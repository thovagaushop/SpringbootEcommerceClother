package com.project.ecommerce.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.dto.ProductRequestDTO;
import com.project.ecommerce.dto.ProductResponseDTO;
import com.project.ecommerce.dto.SuccessResponseDTO;
import com.project.ecommerce.entities.Cart;
import com.project.ecommerce.entities.Category;
import com.project.ecommerce.entities.Product;
import com.project.ecommerce.entities.SpecialCategory;
import com.project.ecommerce.exeptions.APIException;
import com.project.ecommerce.exeptions.ResourceNotFoundException;
import com.project.ecommerce.repositories.CartRepository;
import com.project.ecommerce.repositories.CategoryRepository;
import com.project.ecommerce.repositories.ProductRepository;
import com.project.ecommerce.repositories.SpecialCategoryRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ProductService implements IProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SpecialCategoryRepository specialCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductRequestDTO productRequestDTO) throws IOException {
        Category category = categoryRepository.findById(productRequestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId",
                        productRequestDTO.getCategoryId()));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getTitle().equals(productRequestDTO.getTitle())
                    && products.get(i).getDescription().equals(productRequestDTO.getDescription())) {

                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {            
            Product product = new Product(); // Create a new instance of Product
            product.setCategory(category);
            product.setBrand(productRequestDTO.getBrand());
            product.setTitle(productRequestDTO.getTitle());
            product.setSku(productRequestDTO.getSku());
            product.setRating(productRequestDTO.getRating());
            product.setDescription(productRequestDTO.getDescription());
            product.setPrice(productRequestDTO.getPrice());
            product.setQuantity(productRequestDTO.getQuantity());
            product.setDiscount(productRequestDTO.getDiscount());
            product.setViewNumber(productRequestDTO.getViewNumber());
            product.setBuyNumber(productRequestDTO.getBuyNumber());
            product.setImages(new ArrayList<String>());

            // Set discount and special price
            product.setSpecialPrice(productRequestDTO.getPrice() - (productRequestDTO.getPrice() * productRequestDTO.getDiscount() / 100));

            for(MultipartFile image: productRequestDTO.getImages()) {
                String imageName = fileService.uploadImage(path, image);
                product.getImages().add(imageName);
            }

            Product savedProduct = productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        } else {
            throw new APIException("Product already exists !!!");
        }
    }

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer
    pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
    Sort.by(sortBy).ascending()
    : Sort.by(sortBy).descending();

    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

    Page<Product> pageProducts = productRepository.findAll(pageDetails);

    List<Product> products = pageProducts.getContent();

    List<ProductDTO> productDTOs = products.stream().map(product ->
    modelMapper.map(product, ProductDTO.class))
    .collect(Collectors.toList());

    ProductResponseDTO productResponse = new ProductResponseDTO();

    productResponse.setData(productDTOs);
    productResponse.setPageNumber(pageProducts.getNumber());
    productResponse.setPageSize(pageProducts.getSize());
    productResponse.setTotalElements(pageProducts.getTotalElements());
    productResponse.setTotalPages(pageProducts.getTotalPages());
    productResponse.setLastPage(pageProducts.isLast());

    return productResponse;
    }

    public ProductDTO updateProduct(Long productId, ProductRequestDTO product) throws IOException {
        Product productFromDatabase = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        Category category = categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", product.getCategoryId()));

        productFromDatabase.setCategory(category);
        if (product.getSpecialCategoryId() != null) {
            SpecialCategory specialCategory = specialCategoryRepository.findById(product.getSpecialCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("SpecialCategory", "specialCategoryId",
                            product.getSpecialCategoryId()));
            productFromDatabase.setSpecialCategory(specialCategory);
        }
        productFromDatabase.setBrand(product.getBrand());
        productFromDatabase.setTitle(product.getTitle());
        productFromDatabase.setSku(product.getSku());
        productFromDatabase.setRating(product.getRating());
        productFromDatabase.setDescription(product.getDescription());
        productFromDatabase.setPrice(product.getPrice());
        productFromDatabase.setQuantity(product.getQuantity());
        productFromDatabase.setDiscount(product.getDiscount());
        productFromDatabase.setSpecialPrice(productFromDatabase.getPrice() - (productFromDatabase.getPrice() * productFromDatabase.getDiscount() / 100));
        productFromDatabase.setViewNumber(product.getViewNumber());
        productFromDatabase.setBuyNumber(product.getBuyNumber());

        if (product.getImages() != null) {
            productFromDatabase.setImages(new ArrayList<String>());
            for(MultipartFile image: product.getImages()) {
                String imageName = fileService.uploadImage(path, image);
                productFromDatabase.getImages().add(imageName);
            }
        }

        productRepository.save(productFromDatabase);

        return modelMapper.map(productFromDatabase, ProductDTO.class);
    }
    @Override
    public ProductDTO updateProductWhenRemoveSpecialCategory(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        product.setSpecialCategory(null);

        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public SuccessResponseDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        List<Cart> carts = cartRepository.findCartsByProductId(productId);

        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(),
        productId));
        productRepository.delete(product);
        return new SuccessResponseDTO("success", "Product with productId: " + productId + " deleted successfully !!!");
    }
}

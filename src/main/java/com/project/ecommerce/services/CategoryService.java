package com.project.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.ecommerce.dto.CategoryDTO;
import com.project.ecommerce.dto.PaginationCategoryResponseDTO;
import com.project.ecommerce.dto.PaginationProductResponseDTO;
import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.entities.Category;
import com.project.ecommerce.entities.Product;
import com.project.ecommerce.exeptions.APIException;
import com.project.ecommerce.exeptions.ResourceNotFoundException;
import com.project.ecommerce.repositories.CategoryRepository;
import com.project.ecommerce.repositories.ProductRepository;

@Service
public class CategoryService implements ICategoryService{

    @Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDTO createCategory(Category category) {
		Category savedCategory = categoryRepository.findByName(category.getName());

		if (savedCategory != null) {
			throw new APIException("Category with the name '" + category.getName() + "' already exists !!!");
		}

		savedCategory = categoryRepository.save(category);

		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	@Override
	public PaginationCategoryResponseDTO getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Category> pageCategories = categoryRepository.findAll(pageDetails);

		List<Category> categories = pageCategories.getContent();

		if (categories.size() == 0) {
			throw new APIException("No category is created till now");
		}

		List<CategoryDTO> categoryDTOs = categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());

		PaginationCategoryResponseDTO categoryResponse = new PaginationCategoryResponseDTO();
		
		categoryResponse.setData(categoryDTOs);
		categoryResponse.setPageNumber(pageCategories.getNumber());
		categoryResponse.setPageSize(pageCategories.getSize());
		categoryResponse.setTotalElements(pageCategories.getTotalElements());
		categoryResponse.setTotalPages(pageCategories.getTotalPages());
		categoryResponse.setLastPage(pageCategories.isLast());
		
		return categoryResponse;
	}

	@Override
	public CategoryDTO updateCategory(Category category, Long categoryId) {
		Category savedCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		category.setId(categoryId);

		savedCategory = categoryRepository.save(category);

		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	@Override
	public String deleteCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		List<Product> products = category.getProducts();

		products.forEach(product -> {
			productService.deleteProduct(product.getId());
		});
		
		categoryRepository.delete(category);

		return "Category with categoryId: " + categoryId + " deleted successfully !!!";
	}

	@Override
	public PaginationProductResponseDTO getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Product> products = productRepository.findByCategory(category, pageDetails);

		if (products.getContent().size() == 0) {
			throw new APIException(category.getName() + " category doesn't contain any products !!!");
		}

		List<ProductDTO> productDTOs = products.getContent().stream().map(p -> modelMapper.map(p, ProductDTO.class))
				.collect(Collectors.toList());

		PaginationProductResponseDTO productResponse = new PaginationProductResponseDTO();

		productResponse.setData(productDTOs);
		productResponse.setPageNumber(products.getNumber());
		productResponse.setPageSize(products.getSize());
		productResponse.setTotalElements(products.getTotalElements());
		productResponse.setTotalPages(products.getTotalPages());
		productResponse.setLastPage(products.isLast());

		return productResponse;
	}
}

package com.project.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.ecommerce.dto.CartDTO;
import com.project.ecommerce.dto.CartItemDTO;
import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.dto.SuccessResponseDTO;
import com.project.ecommerce.entities.Cart;
import com.project.ecommerce.entities.CartItem;
import com.project.ecommerce.entities.Product;
import com.project.ecommerce.exeptions.APIException;
import com.project.ecommerce.exeptions.ResourceNotFoundException;
import com.project.ecommerce.repositories.CartItemRepository;
import com.project.ecommerce.repositories.CartRepository;
import com.project.ecommerce.repositories.ProductRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CartService implements ICartItemService{
    @Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ModelMapper modelMapper;

    @Override
	public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) {

		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem != null) {
			throw new APIException("Product " + product.getTitle() + " already exists in the cart");
		}

		if (product.getQuantity() == 0) {
			throw new APIException(product.getTitle() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new APIException("Please, make an order of the " + product.getTitle()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}

		CartItem newCartItem = new CartItem();

		newCartItem.setProduct(product);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());

		cartItemRepository.save(newCartItem);

		product.setQuantity(product.getQuantity() - quantity);

		cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        List<CartItem> cartItems = cart.getCartItems();
        cartItems.add(newCartItem);
        cart.setCartItems(cartItems);

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

		List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p, CartItemDTO.class)).collect(Collectors.toList());

		cartDTO.setProducts(cartItemDTOs);

		return cartDTO;

	}

	// @Override
	// public List<CartDTO> getAllCarts() {
	// 	List<Cart> carts = cartRepository.findAll();

	// 	if (carts.size() == 0) {
	// 		throw new APIException("No cart exists");
	// 	}

	// 	List<CartDTO> cartDTOs = carts.stream().map(cart -> {
	// 		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

	// 		List<ProductDTO> products = cart.getCartItems().stream()
	// 				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

	// 		cartDTO.setProducts(products);

	// 		return cartDTO;

	// 	}).collect(Collectors.toList());

	// 	return cartDTOs;
	// }

	@Override
	public CartDTO getCart(String emailId) {
		Cart cart = cartRepository.findCartByEmail(emailId);

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", emailId);
		}

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p, CartItemDTO.class)).collect(Collectors.toList());

		cartDTO.setProducts(cartItemDTOs);

		return cartDTO;
	}

	@Override
	public void updateProductInCarts(Long cartId, Long productId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new APIException("Product " + product.getTitle() + " not available in the cart!!!");
		}

		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

		cartItem.setProductPrice(product.getPrice());

		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

		cartItem = cartItemRepository.save(cartItem);
	}

	@Override
	public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		if (product.getQuantity() == 0) {
			throw new APIException(product.getTitle() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new APIException("Please, make an order of the " + product.getTitle()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new APIException("Product " + product.getTitle() + " not available in the cart!!!");
		}

		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

		product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);

		cartItem.setProductPrice(product.getPrice());
		cartItem.setQuantity(quantity);

		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));

		cartItem = cartItemRepository.save(cartItem);

		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

		List<CartItemDTO> cartItemDTOs = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p, CartItemDTO.class)).collect(Collectors.toList());

		cartDTO.setProducts(cartItemDTOs);

		return cartDTO;

	}

	@Override
	public SuccessResponseDTO deleteProductFromCart(Long cartId, Long productId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new ResourceNotFoundException("Product", "productId", productId);
		}

		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

		Product product = cartItem.getProduct();
		product.setQuantity(product.getQuantity() + cartItem.getQuantity());

		cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

		return new SuccessResponseDTO("success", "Product " + cartItem.getProduct().getTitle() + " removed from the cart !!!");
	}
}

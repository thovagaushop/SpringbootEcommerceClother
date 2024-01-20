package com.project.ecommerce.services;

import java.util.List;

import com.project.ecommerce.dto.CartDTO;

public interface ICartService {
    CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);
	
	// List<CartDTO> getAllCarts();
	
	CartDTO getCart(String emailId);
	
	CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);
	
	void updateProductInCarts(Long cartId, Long productId);
	
	String deleteProductFromCart(Long cartId, Long productId);
}

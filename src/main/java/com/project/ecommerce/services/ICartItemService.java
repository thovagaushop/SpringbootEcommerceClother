package com.project.ecommerce.services;

import java.util.List;

import com.project.ecommerce.dto.CartDTO;
import com.project.ecommerce.dto.SuccessResponseDTO;

public interface ICartItemService {
    CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);
	
	// List<CartDTO> getAllCarts();
	
	CartDTO getCart(String emailId);
	
	CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);
	
	void updateProductInCarts(Long cartId, Long productId);
	
	SuccessResponseDTO deleteProductFromCart(Long cartId, Long productId);
}

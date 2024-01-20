package com.project.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.CartDTO;
import com.project.ecommerce.dto.SuccessResponseDTO;
import com.project.ecommerce.services.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("")
    public ResponseEntity<CartDTO> getCart(
        @RequestParam(name = "email", required = true) String email
    ) {
        CartDTO cartDTO = cartService.getCart(email);
        return ResponseEntity.ok(cartDTO);
    }

    @PostMapping("/{cartId}/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
        @PathVariable Long cartId,
        @PathVariable Long productId,
        @PathVariable Integer quantity
    ) {
        CartDTO cartDTO = cartService.addProductToCart(cartId, productId, quantity);
        return ResponseEntity.ok(cartDTO);
    }

    @PutMapping("/{cartId}/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> updateProductQuantityIncart(
        @PathVariable Long cartId,
        @PathVariable Long productId,
        @PathVariable Integer quantity
    ) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/{cartId}/{productId}")
    public ResponseEntity<SuccessResponseDTO> deleteProductInCart(
        @PathVariable Long cartId,
        @PathVariable Long productId
    ) {
        SuccessResponseDTO responseSuccess = cartService.deleteProductFromCart(cartId, productId);
        return ResponseEntity.ok(responseSuccess);
    }
}

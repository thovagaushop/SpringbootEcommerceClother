package com.project.ecommerce.dto;

import com.project.ecommerce.entities.Cart;
import com.project.ecommerce.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Long productId;
    private String productTitle;
    private Integer quantity;
    private float discount;
    private double productSpecialPrice;
}

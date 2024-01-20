package com.project.ecommerce.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
	private Long categoryId;
	private Long specialCategoryId;
	private String brand;
	private String title;
	private String sku;
	private Integer rating;
	private String description;
	private double price;
	private Integer quantity;
	private float discount;
	private double specialPrice;
	private Integer viewNumber;
	private Integer buyNumber;
	private List<String> images;
}

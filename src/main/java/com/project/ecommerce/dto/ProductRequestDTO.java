package com.project.ecommerce.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    @NotNull(message = "Category id is required")
    private Long categoryId;
    private Long specialCategoryId = null;


    private String brand;

    @NotBlank(message = "Product title is required")
    @Size(min = 5, max = 100, message = "Product title must be between 5 and 100 characters length")
    private String title;
    private String sku;

    @Min(value = 0, message = "Rating value must be greater than 0")
    @Max(value = 5, message = "Rating value must be less than 5")
    private Integer rating = 5;

    @NotBlank(message = "Product description is required")
    @Size(min = 6, max = 255, message = "Product description must between 6 and 255 characters length")
    private String description;

    @NotNull(message = "Product price is required")
    @Min(value = 0, message = "Price must be greater than 0")
    private Double price;

    @Nullable
    @Min(value = 0, message = "Product quantity must be greater than 0")
    private Integer quantity = 0;

    @Min(value = 0, message = "Product discount must be greater than 0")
    @Max(value = 100, message = "Product discount must be less than 0")
    private float discount;

    @Min(value = 0, message = "Special Price must be greater than 0")
    private double specialPrice;

    private int viewNumber = 0;

    private int buyNumber = 0;

    @Nullable
    @Size(min = 3, max = 3, message = "Product images must be 3 images")
    private List<MultipartFile> images;

    public ProductRequestDTO(Long categoryId, String title, String description, Double price) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.price = price;
    }
}

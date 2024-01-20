package com.project.ecommerce.entities;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

    @ManyToOne
    @JoinColumn(name = "special_category_id")
    private SpecialCategory specialCategory;

    @Nullable
    private String brand;

    @NotBlank
    @Size(min = 5, max = 100, message = "Product title must be between 5 and 100 characters length")
    @Column(nullable = false, unique = true)
    private String title;

    @Nullable
    private String sku;

    @NotNull
    @Min(value = 0, message = "Rating value must be greater than 0")
    @Max(value = 5, message = "Rating value must be less than 5")
    @Column(nullable = false)
    private Integer rating = 5;

    @NotBlank
    @Size(min = 6, max = 255, message = "Product description must between 6 and 255 characters length")
    @Column(nullable = false)
    private String description;

    @NotNull
    @Min(value = 0, message = "Price must be greater than 0")
    @Column(nullable = false)
    private double price;

    @NotNull
    @Min(value = 0, message = "Product quantity must be greater than 0")
    @Column(nullable = false)
    private Integer quantity = 0;
    
    @NotNull
    @Min(value = 0, message = "Product discount must be greater than 0 ")
    @Max(value = 100, message = "Product discount must be less than 100")
    @Column(nullable = false)
    private float discount;
    
    @Nullable
    @Min(value = 0, message = "Special Price must be greater than 0")
    @Column(nullable = true, name = "special_price")
    private double specialPrice;

    @Column(name = "view_number", nullable = false)
    private int viewNumber = 0;

    @Column(name = "buy_number", nullable = false)
    private int buyNumber = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;
}

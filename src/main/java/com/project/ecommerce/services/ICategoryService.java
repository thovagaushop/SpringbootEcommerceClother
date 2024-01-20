package com.project.ecommerce.services;

import java.util.List;

import com.project.ecommerce.dto.CategoryDTO;
import com.project.ecommerce.dto.PaginationCategoryResponseDTO;
import com.project.ecommerce.dto.PaginationProductResponseDTO;
import com.project.ecommerce.entities.Category;

public interface ICategoryService {
    CategoryDTO createCategory(Category category);

	PaginationCategoryResponseDTO getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	CategoryDTO updateCategory(Category category, Long categoryId);

	String deleteCategory(Long categoryId);

	PaginationProductResponseDTO getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}

package com.project.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.CategoryDTO;
import com.project.ecommerce.dto.CategoryRequestDTO;
import com.project.ecommerce.dto.PaginationCategoryResponseDTO;
import com.project.ecommerce.dto.PaginationProductResponseDTO;
import com.project.ecommerce.dto.SuccessResponseDTO;
import com.project.ecommerce.entities.Category;
import com.project.ecommerce.services.CategoryService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequestDTO body) {
        Category category = new Category();
        category.setName(body.getName());
        
        CategoryDTO categoryDTO = categoryService.createCategory(category);
        
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("")
    public ResponseEntity<PaginationCategoryResponseDTO> getCategories(
        @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder
    ) {
        PaginationCategoryResponseDTO categoryResponseDTO = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<PaginationProductResponseDTO> getProductByCategory(
        @PathVariable Long id,
        @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
		@RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize,
		@RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
		@RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder
    ) {
        System.out.println(id);
        PaginationProductResponseDTO productDTOs = categoryService.getProductsByCategory(id, pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(productDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody Category body, @PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.updateCategory(body, id);

        return ResponseEntity.ok(categoryDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseDTO>  deleteCategory(@PathVariable Long id) {
        String message = categoryService.deleteCategory(id);
        return ResponseEntity.ok(new SuccessResponseDTO("success", message));
    }
}

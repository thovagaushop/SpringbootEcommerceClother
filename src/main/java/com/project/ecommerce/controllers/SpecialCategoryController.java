package com.project.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.CategoryDTO;
import com.project.ecommerce.dto.CategoryRequestDTO;
import com.project.ecommerce.dto.PaginationCategoryResponseDTO;
import com.project.ecommerce.dto.SuccessResponseDTO;
import com.project.ecommerce.entities.SpecialCategory;
import com.project.ecommerce.services.SpecialCategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/special-category")
public class SpecialCategoryController {
    @Autowired
    private SpecialCategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryRequestDTO body) {
        SpecialCategory category = new SpecialCategory();
        category.setName(body.getName());
        
        CategoryDTO categoryDTO = categoryService.createCategory(category);
        
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("")
    public ResponseEntity<PaginationCategoryResponseDTO> getMethodName(@RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
    @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize,
    @RequestParam(name = "sortBy", defaultValue = "id", required = false) String sortBy,
    @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder) {
        PaginationCategoryResponseDTO categoryResponseDTO = categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(categoryResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody SpecialCategory body, @PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.updateCategory(body, id);

        return ResponseEntity.ok(categoryDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseDTO>  deleteCategory(@PathVariable Long id) {
        String message = categoryService.deleteCategory(id);
        return ResponseEntity.ok(new SuccessResponseDTO("success", message));
    }
}

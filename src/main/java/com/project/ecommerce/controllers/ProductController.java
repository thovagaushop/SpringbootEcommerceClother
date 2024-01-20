package com.project.ecommerce.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.ProductDTO;
import com.project.ecommerce.dto.ProductRequestDTO;
import com.project.ecommerce.dto.ProductResponseDTO;
import com.project.ecommerce.dto.SuccessResponseDTO;
import com.project.ecommerce.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public ResponseEntity<ProductResponseDTO> getAllProducts(
        @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
		@RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize,
		@RequestParam(name = "sortBy", defaultValue = "title", required = false) String sortBy,
		@RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder)
    {
        ProductResponseDTO productResponseDTO = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        return ResponseEntity.ok(productResponseDTO);
    }

    @PostMapping("")
    public ResponseEntity<ProductDTO> addProduct(
        @Valid 
        @ModelAttribute 
        ProductRequestDTO productRequestDTO
    ) throws IOException {
        ProductDTO productDTO = productService.addProduct(productRequestDTO);

        return ResponseEntity.ok(productDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
        @Valid 
        @ModelAttribute 
        ProductRequestDTO productRequestDTO ,
        @PathVariable Long id
    ) throws IOException {
        ProductDTO productDTO = productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.ok(productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseDTO> delelteProduct (
        @PathVariable Long id
    ) {
        SuccessResponseDTO successResponseDTO = productService.deleteProduct(id);
        return ResponseEntity.ok(successResponseDTO);
    }
}

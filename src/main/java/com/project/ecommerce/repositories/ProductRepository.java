package com.project.ecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommerce.entities.Category;
import com.project.ecommerce.entities.Product;
public interface ProductRepository extends JpaRepository<Product, Long>{
    Page<Product> findByTitle(String keyword, Pageable pageDetails);
    Page<Product> findByCategory(Category category, Pageable pageDetails);
}

package com.project.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.ecommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    Category findByName(String name);
}

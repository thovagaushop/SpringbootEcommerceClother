package com.project.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommerce.entities.SpecialCategory;

public interface SpecialCategoryRepository extends JpaRepository<SpecialCategory, Long>{
    SpecialCategory findByName(String name);
}

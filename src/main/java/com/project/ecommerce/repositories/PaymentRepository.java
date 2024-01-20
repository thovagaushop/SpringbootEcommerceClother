package com.project.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommerce.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
}

package com.project.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ecommerce.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long>{
    
}

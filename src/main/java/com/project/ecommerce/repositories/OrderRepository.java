package com.project.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ecommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.email = ?1 AND o.id = ?2")
    Order findOrderByEmailAndOrderId(String email, Long orderId);

    List<Order> findAllByEmail(String email);
}

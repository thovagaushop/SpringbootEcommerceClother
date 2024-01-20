package com.project.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.entities.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
	Cart findCartByEmail(String email);

	@Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
	List<Cart> findCartsByProductId(Long productId);
}

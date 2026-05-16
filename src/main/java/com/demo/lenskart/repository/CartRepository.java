package com.demo.lenskart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.lenskart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	Cart findByCustomerUserId(int userId);
}

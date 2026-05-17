package com.demo.shopwave.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.shopwave.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	Cart findByCustomerUserId(int userId);
}

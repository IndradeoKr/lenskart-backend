package com.demo.lenskart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.lenskart.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
	List<Orders> findByCartCustomerUserId(int customerId);

	boolean existsByCartId(int cartId);

	List<Orders> findByCartProductCategoryCategoryId(int categoryId); // if relevant
}

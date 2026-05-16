package com.demo.lenskart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.lenskart.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	List<Product> findByBrand(String brand);
}

package com.demo.shopwave.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.shopwave.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findByCategoryName(String categoryName);
}
package com.demo.lenskart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.lenskart.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findByCategoryName(String categoryName);
}
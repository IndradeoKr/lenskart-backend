package com.demo.shopwave.dto;

import jakarta.validation.constraints.*;

public class CategoryDTO {

	private int categoryId;

	@NotBlank(message = "Category name is required")
	private String categoryName;

	// Getters and setters
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
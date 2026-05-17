package com.demo.shopwave.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductDTO {

	private int productId;

	@NotBlank(message = "Product name is required")
	private String productName;

	@DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
	private double productPrice;

	@NotBlank(message = "Product image URL is required")
	private String productImage;

	@Min(value = 0, message = "Quantity cannot be negative")
	private int quantity;

	@NotNull(message = "Category must be selected")
	private String categoryName;

	@NotBlank(message = "Brand name is required")
	private String brand;

	// Getters and setters
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCategory() {
		return categoryName;
	}

	public void setCategory(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}

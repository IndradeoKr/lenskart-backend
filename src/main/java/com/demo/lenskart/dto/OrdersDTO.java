package com.demo.lenskart.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class OrdersDTO {

	private int orderId;

	@NotNull(message = "Order date is required")
	private LocalDateTime date;

	public enum Status {
		IN_PROGRESS, DELIVERED
	}

	@NotNull(message = "Order status is required")
	private Status status;

	@NotNull(message = "Cart must be associated with order")
	private int cartId;

	private String customerEmail;

	// Getters and setters
	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
}

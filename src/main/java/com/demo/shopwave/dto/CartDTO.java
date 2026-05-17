/*
 * package com.demp.lenskart.dto;
 * 
 * import java.util.List;
 * 
 * import jakarta.validation.constraints.DecimalMin; import
 * jakarta.validation.constraints.Min; import
 * jakarta.validation.constraints.NotNull; import
 * jakarta.validation.constraints.Size;
 * 
 * public class CartDTO {
 * 
 * private int id;
 * 
 * @NotNull(message = "Product list cannot be null")
 * 
 * @Size(min = 1, message = "Cart must have at least one product") private
 * List<List<Integer>> listOfProductId;
 * 
 * @NotNull(message = "Customer cannot be null") private int customerId;
 * 
 * @Min(value = 1, message = "Total quantity must be at least 1") private int
 * totalQuantity;
 * 
 * @DecimalMin(value = "0.0", inclusive = true, message =
 * "Total price must be non-negative") private double totalPrice;
 * 
 * public int getId() { return id; }
 * 
 * public void setId(int id) { this.id = id; }
 * 
 * public List<List<Integer>> getProduct() { return listOfProductId; }
 * 
 * public void setProduct(List<List<Integer>> listOfProductId) {
 * this.listOfProductId = listOfProductId; }
 * 
 * public int getCustomer() { return customerId; }
 * 
 * public void setCustomer(int customerId) { this.customerId = customerId; }
 * 
 * public int getTotalQuantity() { return totalQuantity; }
 * 
 * public void setTotalQuantity(int totalQuantity) { this.totalQuantity =
 * totalQuantity; }
 * 
 * public double getTotalPrice() { return totalPrice; }
 * 
 * public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice;
 * } }
 */

package com.demo.shopwave.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartDTO {

	private int id;

	@NotNull(message = "Product list cannot be null")
	private int productId;

	@NotNull(message = "Customer cannot be null")
	private int customerId;

	@Min(value = 1, message = "Total quantity must be at least 1")
	@Max(value = 999, message = "Total quantity can be at most 999")
	private int quantity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
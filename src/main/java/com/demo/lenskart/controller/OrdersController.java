package com.demo.lenskart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.lenskart.dto.OrdersDTO;
import com.demo.lenskart.service.IOrdersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders Operations", description = "Endpoints for managing shopping order actions such as adding, updating, and removing items.")
public class OrdersController {

	private static final Logger logger = LoggerFactory.getLogger(OrdersController.class);

	private IOrdersService ordersService;

	@Autowired
	public OrdersController(IOrdersService ordersService) {
		this.ordersService = ordersService;
	}

	@Operation(summary = "Place Order", description = "Place an order for a user with items in cart. Requires valid email and password.")
	@PostMapping
	public ResponseEntity<String> addOrder(@RequestBody @Valid OrdersDTO ordersDTO) {
		logger.info("Received request to place order for cart ID: {}", ordersDTO.getCartId());
		return ordersService.addOrders(ordersDTO);
	}

	@Operation(summary = "Update Order", description = "Update the status of the order")
	@PutMapping
	public ResponseEntity<OrdersDTO> updateOrder(@RequestBody @Valid OrdersDTO ordersDTO) {
		logger.info("Received request to update order with Order ID: {}", ordersDTO.getOrderId());
		return ordersService.updateOrders(ordersDTO);
	}

	@Operation(summary = "Delete Order", description = "Delete the order by order ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable int id) {
		logger.info("Received request to delete order with Order ID: {}", id);
		return ordersService.deleteOrders(id);
	}

	@Operation(summary = "Retrieve all orders", description = "Fetch all orders from system")
	@GetMapping
	public ResponseEntity<List<OrdersDTO>> getAllOrders() {
		logger.info("Received request to fetch all orders");
		return ordersService.findAll();
	}

	@Operation(summary = "Retrieve orders by Customer ID", description = "Fetch orders made by a specific customer")
	@GetMapping("/{customerId}")
	public ResponseEntity<List<OrdersDTO>> getOrdersByCustomerId(@PathVariable int customerId) {
		logger.info("Received request to fetch orders for Customer ID: {}", customerId);
		return ordersService.getOrderCustomerId(customerId);
	}
}
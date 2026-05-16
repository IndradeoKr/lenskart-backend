package com.demo.lenskart.serviceimplement;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.demo.lenskart.dto.OrdersDTO;
import com.demo.lenskart.dto.OrdersDTO.Status;
import com.demo.lenskart.entity.Cart;
import com.demo.lenskart.entity.Orders;
import com.demo.lenskart.entity.Product;
import com.demo.lenskart.entity.User;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.CartRepository;
import com.demo.lenskart.repository.OrdersRepository;
import com.demo.lenskart.service.IOrdersService;

import jakarta.transaction.Transactional;

@Service
public class OrdersServiceImpl implements IOrdersService {

	private static final Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);

	private OrdersRepository ordersRepository;
	private CartRepository cartRepository;

	@Autowired
	public OrdersServiceImpl(OrdersRepository ordersRepository, CartRepository cartRepository) {
		this.ordersRepository = ordersRepository;
		this.cartRepository = cartRepository;
	}

	@Override
	@Transactional
	public ResponseEntity<String> addOrders(OrdersDTO ordersDTO) {
		logger.info("Attempting to place order for Cart ID: {}", ordersDTO.getCartId());

		Cart cart = cartRepository.findById(ordersDTO.getCartId()).orElseThrow(() -> {
			logger.error("Cart not found with ID: {}", ordersDTO.getCartId());
			return new LenskartApplicationException("Cart not found with this ID");
		});

		int orderQuantity = cart.getTotalQuantity();
		Product product = cart.getProduct().get(0);
		if (orderQuantity <= 0) {
			logger.warn("Product stock insufficient for Cart ID: {}", cart.getId());
			throw new LenskartApplicationException("order cant't be placed due to unavailability");
		}
		if (product.getQuantity() < orderQuantity) {
			throw new LenskartApplicationException("product is out of stock");
		}

		product.setQuantity(product.getQuantity() - orderQuantity);
		cart.setTotalPrice(0);
		cart.setTotalQuantity(0);

		if (ordersDTO.getDate() == null) {
			logger.error("Order date is missing for Cart ID: {}", cart.getId());
			throw new LenskartApplicationException("Invalid order date");
		}

		Orders order = new Orders();
		order.setDate(ordersDTO.getDate());
		order.setStatus(Orders.Status.IN_PROGRESS);
		order.setCart(cart);
		order.setUserId(cart.getCustomer());
		order.setOrderedQuantity(orderQuantity);

		Orders saved = ordersRepository.save(order);
		ordersDTO.setOrderId(saved.getOrderId());

		logger.info("Order placed successfully with Order ID: {}", saved.getOrderId());
		return ResponseEntity.status(HttpStatus.CREATED)
				.body("Order added successfully with ID: " + saved.getOrderId());
	}

	@Override
	@Transactional
	public ResponseEntity<OrdersDTO> updateOrders(OrdersDTO ordersDTO) {
		logger.info("Request received to update Order ID: {}", ordersDTO.getOrderId());

		if (ordersDTO.getOrderId() <= 0) {
			logger.error("Invalid order ID provided");
			throw new LenskartApplicationException("Invalid Order ID");
		}

		Orders order = ordersRepository.findById(ordersDTO.getOrderId()).orElse(null);
		if (order == null) {
			logger.error("Order not found with ID: {}", ordersDTO.getOrderId());
			throw new LenskartApplicationException("Order with ID " + ordersDTO.getOrderId() + " not found");
		}

		if (ordersDTO.getDate() == null) {
			logger.error("Missing date for Order ID: {}", ordersDTO.getOrderId());
			throw new LenskartApplicationException("Order date cannot be null");
		}

		if (ordersDTO.getStatus() != null) {
			if (order.getStatus() == Orders.Status.DELIVERED && ordersDTO.getStatus() == OrdersDTO.Status.IN_PROGRESS) {
				throw new LenskartApplicationException("Cannot change status from DELIVERED to IN_PROGRESS");
			}
			order.setStatus(Orders.Status.valueOf(ordersDTO.getStatus().name()));
		}

		order.setDate(ordersDTO.getDate());
		Orders updated = ordersRepository.save(order);

		OrdersDTO updatedDTO = new OrdersDTO();
		updatedDTO.setOrderId(updated.getOrderId());
		updatedDTO.setDate(updated.getDate());
		updatedDTO.setCartId(updated.getCart().getId());
		if (updated.getStatus() != null) {
			updatedDTO.setStatus(Status.valueOf(updated.getStatus().name()));
		}

		logger.info("Order updated successfully for Order ID: {}", updated.getOrderId());
		return ResponseEntity.status(HttpStatus.OK).body(updatedDTO);
	}

	@Override
	@Transactional
	public ResponseEntity<String> deleteOrders(int orderId) {
		logger.info("Request received to delete Order ID: {}", orderId);

		Orders order = ordersRepository.findById(orderId).orElse(null);
		if (order == null) {
			logger.warn("Attempt to delete non-existent Order ID: {}", orderId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order with ID " + orderId + " not found");
		}

		// Treat delete as "cancel": restore stock before removing the order.
		try {
			Cart cart = order.getCart();
			if (cart != null && cart.getProduct() != null && !cart.getProduct().isEmpty()) {
				Product product = cart.getProduct().get(0);
				int qty = order.getOrderedQuantity();
				if (qty > 0) {
					product.setQuantity(product.getQuantity() + qty);
				}
			}
		} catch (Exception e) {
			logger.warn("Unable to restore stock for Order ID {}: {}", orderId, e.getMessage());
		}

		ordersRepository.deleteById(orderId);
		logger.info("Order deleted successfully for Order ID: {}", orderId);

		return ResponseEntity.ok("Order with ID " + orderId + " deleted successfully");
	}

	@Override
	public ResponseEntity<List<OrdersDTO>> findAll() {
		logger.info("Fetching all orders from repository");

		List<Orders> ordersList = ordersRepository.findAll();
		if (ordersList.isEmpty()) {
			logger.warn("No orders found in the system");
			throw new LenskartApplicationException("No orders found in the system");
		}

		List<OrdersDTO> dtos = new ArrayList<>();
		for (Orders order : ordersList) {
			OrdersDTO dto = new OrdersDTO();
			dto.setOrderId(order.getOrderId());
			dto.setDate(order.getDate());
			dto.setCartId(order.getCart().getId());
			if (order.getStatus() != null) {
				dto.setStatus(Status.valueOf(order.getStatus().name()));
			}
			dtos.add(dto);
		}

		logger.info("Total orders retrieved: {}", dtos.size());
		return ResponseEntity.ok(dtos);
	}

	@Override
	public ResponseEntity<List<OrdersDTO>> getOrderCustomerId(int customerId) {
		logger.info("Fetching orders for Customer ID: {}", customerId);

		if (customerId <= 0) {
			logger.error("Invalid Customer ID: {}", customerId);
			throw new LenskartApplicationException("Customer ID must be a positive integer");
		}

		List<Orders> ordersList = ordersRepository.findByCartCustomerUserId(customerId);
		if (ordersList == null || ordersList.isEmpty()) {
			logger.warn("No orders found for Customer ID: {}", customerId);
			throw new LenskartApplicationException("No orders found for customer ID: " + customerId);
		}

		List<OrdersDTO> dtos = new ArrayList<>();
		for (Orders order : ordersList) {
			OrdersDTO dto = new OrdersDTO();
			dto.setOrderId(order.getOrderId());
			dto.setDate(order.getDate());
			dto.setCartId(order.getCart().getId());
			if (order.getStatus() != null) {
				dto.setStatus(Status.valueOf(order.getStatus().name()));
			}
			dtos.add(dto);
		}

		logger.info("Orders retrieved for Customer ID {}: {}", customerId, dtos.size());
		return ResponseEntity.ok(dtos);
	}
}

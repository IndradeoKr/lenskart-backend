package com.demo.lenskart.service;

import java.util.List;
import org.springframework.http.ResponseEntity;

import com.demo.lenskart.dto.OrdersDTO;

public interface IOrdersService {
	ResponseEntity<String> addOrders(OrdersDTO ordersDTO);

	ResponseEntity<OrdersDTO> updateOrders(OrdersDTO ordersDTO);

	ResponseEntity<String> deleteOrders(int orderId);

	ResponseEntity<List<OrdersDTO>> findAll();

	ResponseEntity<List<OrdersDTO>> getOrderCustomerId(int customerId);
}

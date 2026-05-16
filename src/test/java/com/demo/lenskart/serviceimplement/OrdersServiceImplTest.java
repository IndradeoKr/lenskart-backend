package com.demo.lenskart.serviceimplement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;

import com.demo.lenskart.dto.OrdersDTO;
import com.demo.lenskart.entity.*;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.*;

@ExtendWith(MockitoExtension.class)
class OrdersServiceImplTest {

    @Mock private OrdersRepository ordersRepository;
    @Mock private CartRepository cartRepository;

    @InjectMocks private OrdersServiceImpl service;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserid(100);
        user.setOrders(new ArrayList<>());

        product = new Product();
        product.setQuantity(10);

        cart = new Cart();
        cart.setId(55);
        cart.setTotalQuantity(3);
        cart.setTotalPrice(300);
        cart.setCustomer(user);
        cart.setProduct(List.of(product));
    }

    // ----------- addOrders -----------

    @Test
    @DisplayName("addOrders → valid request returns 201")
    void addOrdersSuccess() {
        OrdersDTO dto = new OrdersDTO();
        dto.setCartId(55);
        dto.setDate(LocalDateTime.of(2025, 7, 28, 10, 0, 0));

        when(cartRepository.findById(55)).thenReturn(Optional.of(cart));
        Orders saved = new Orders();
        saved.setOrderId(999);
        when(ordersRepository.save(any())).thenReturn(saved);

        ResponseEntity<String> response = service.addOrders(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("ID: 999");
        assertThat(product.getQuantity()).isEqualTo(7);
        assertThat(cart.getTotalQuantity()).isZero();
        assertThat(cart.getTotalPrice()).isZero();
    }

    @Test
    @DisplayName("addOrders → cart not found throws exception")
    void addOrdersMissingCart() {
        when(cartRepository.findById(99)).thenReturn(Optional.empty());

        OrdersDTO dto = new OrdersDTO();
        dto.setCartId(99);
        dto.setDate(LocalDateTime.now());

        LenskartApplicationException ex = assertThrows(LenskartApplicationException.class, () -> {
            service.addOrders(dto);
        });

        assertThat(ex.getMessage()).contains("Cart not found");
    }

    @Test
    @DisplayName("addOrders → null date throws exception")
    void addOrdersNullDate() {
        when(cartRepository.findById(55)).thenReturn(Optional.of(cart));

        OrdersDTO dto = new OrdersDTO();
        dto.setCartId(55);
        dto.setDate(null);

        LenskartApplicationException ex = assertThrows(LenskartApplicationException.class, () -> {
            service.addOrders(dto);
        });

        assertThat(ex.getMessage()).contains("Invalid order date");
    }

    // ----------- updateOrders -----------

    @Test
    @DisplayName("updateOrders → valid update returns updated DTO")
    void updateOrdersSuccess() {
        OrdersDTO dto = new OrdersDTO();
        dto.setOrderId(101);
        dto.setDate(LocalDateTime.of(2025, 7, 28, 10, 30, 0));

        Orders order = new Orders();
        order.setOrderId(101);
        order.setDate(LocalDateTime.of(2025, 7, 20, 8, 0, 0));
        order.setCart(cart);

        when(ordersRepository.findById(101)).thenReturn(Optional.of(order));
        when(ordersRepository.save(any())).thenReturn(order);

        ResponseEntity<OrdersDTO> response = service.updateOrders(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getOrderId()).isEqualTo(101);
        assertThat(response.getBody().getCartId()).isEqualTo(cart.getId());
        assertThat(response.getBody().getStatus()).isEqualTo(OrdersDTO.Status.DELIVERED);
    }

    @Test
    @DisplayName("updateOrders → missing order throws exception")
    void updateOrdersInvalidId() {
        OrdersDTO dto = new OrdersDTO();
        dto.setOrderId(404);
        dto.setDate(LocalDateTime.now());

        when(ordersRepository.findById(404)).thenReturn(Optional.empty());

        LenskartApplicationException ex = assertThrows(LenskartApplicationException.class, () -> {
            service.updateOrders(dto);
        });

        assertThat(ex.getMessage()).contains("not found");
    }

    @Test
    @DisplayName("updateOrders → null date throws exception")
    void updateOrdersNullDate() {
        Orders order = new Orders();
        order.setOrderId(101);
        order.setCart(cart);

        when(ordersRepository.findById(101)).thenReturn(Optional.of(order));

        OrdersDTO dto = new OrdersDTO();
        dto.setOrderId(101);
        dto.setDate(null);

        LenskartApplicationException ex = assertThrows(LenskartApplicationException.class, () -> {
            service.updateOrders(dto);
        });

        assertThat(ex.getMessage()).contains("cannot be null");
    }

    // ----------- deleteOrders -----------

    @Test
    @DisplayName("deleteOrders → valid ID removes order and returns 200")
    void deleteOrdersSuccess() {
        Orders order = new Orders();
        order.setOrderId(777);
        order.setUserId(user);

        user.getOrders().add(order);

        when(ordersRepository.findById(777)).thenReturn(Optional.of(order));

        ResponseEntity<String> response = service.deleteOrders(777);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("deleted successfully");
        assertThat(user.getOrders()).doesNotContain(order);
        verify(ordersRepository).deleteById(777);
    }

    @Test
    @DisplayName("deleteOrders → non-existent ID returns 404")
    void deleteOrdersMissingId() {
        when(ordersRepository.findById(888)).thenReturn(Optional.empty());

        ResponseEntity<String> response = service.deleteOrders(888);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(ordersRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("deleteOrders → null user returns 422")
    void deleteOrdersUserNull() {
        Orders order = new Orders();
        order.setOrderId(999);
        order.setUserId(null);

        when(ordersRepository.findById(999)).thenReturn(Optional.of(order));

        ResponseEntity<String> response = service.deleteOrders(999);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        verify(ordersRepository, never()).deleteById(anyInt());
    }

    // ----------- getOrderCustomerId -----------

    @Test
    @DisplayName("getOrderCustomerId → valid ID returns list")
    void getOrderCustomerIdSuccess() {
        Orders order = new Orders();
        order.setOrderId(888);
        order.setDate(LocalDateTime.now());
        order.setCart(cart);

        when(ordersRepository.findByCartCustomerUserId(100)).thenReturn(List.of(order));

        ResponseEntity<List<OrdersDTO>> response = service.getOrderCustomerId(100);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getOrderId()).isEqualTo(888);
    }

    @Test
    @DisplayName("getOrderCustomerId → invalid customer ID throws exception")
    void getOrderCustomerIdInvalid() {
        LenskartApplicationException ex = assertThrows(LenskartApplicationException.class, () -> {
            service.getOrderCustomerId(0);
        });

        assertThat(ex.getMessage()).contains("must be a positive integer");
    }

    @Test
    @DisplayName("getOrderCustomerId → no orders found throws exception")
    void getOrderCustomerIdEmpty() {
        int userId = 200;

        when(ordersRepository.findByCartCustomerUserId(userId)).thenReturn(Collections.emptyList());

        LenskartApplicationException ex = assertThrows(LenskartApplicationException.class, () -> {
            service.getOrderCustomerId(userId);
        });

        assertThat(ex.getMessage()).contains("No orders found for customer ID: " + userId);
    }
}
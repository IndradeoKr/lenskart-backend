package com.demo.lenskart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.demo.lenskart.dto.CartDTO;
import com.demo.lenskart.dto.CartViewDTO;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.service.ICartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart Operations", description = "Endpoints for managing shopping cart actions such as adding, updating, and deleting items.")
public class CartController {
	private static final Logger logger = LoggerFactory.getLogger(CartController.class);

	private ICartService cartService;

	@Autowired
	public CartController(ICartService cartService) {
		this.cartService = cartService;
	}

	@Operation(summary = "Create Cart Entry")
	@PostMapping
	public ResponseEntity<String> addToCart(@RequestBody @Valid CartDTO cartDTO) throws LenskartApplicationException {
		logger.info("Received request to add item to cart: customerId={}, productId={}, quantity={}",
				cartDTO.getCustomerId(), cartDTO.getProductId(), cartDTO.getQuantity());
		String createdCartMessage = cartService.addToCart(cartDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCartMessage);
	}

	@Operation(summary = "Increase Cart Quantity")
	@PutMapping
	public ResponseEntity<String> increaseQuantity(@RequestBody @Valid CartDTO cartDTO) {
		String updatedCart = cartService.increaseCartQuantity(cartDTO);
		if (updatedCart != null) {
			return ResponseEntity.ok(updatedCart);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found");
	}

	@Operation(summary = "Decrease Cart Quantity")
	@PatchMapping
	public ResponseEntity<String> decreaseQuantity(@RequestBody @Valid CartDTO cartDTO) {
		String updatedCart = cartService.decreaseCartQuantity(cartDTO);
		if (updatedCart != null) {
			return ResponseEntity.ok(updatedCart);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to decrease quantity");
	}

	@Operation(summary = "delete Cart Entry")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> removeCart(@PathVariable int id) throws LenskartApplicationException {
		logger.info("Received request to delete cart with cartId={}", id);
		String cartDeletedMessage = cartService.deleteCart(id);
		return ResponseEntity.status(HttpStatus.OK).body(cartDeletedMessage);

	}

	@Operation(summary = "Get Cart For Customer")
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<CartViewDTO> getCartForCustomer(@PathVariable int customerId) throws LenskartApplicationException {
		return ResponseEntity.ok(cartService.getCartForCustomer(customerId));
	}
}

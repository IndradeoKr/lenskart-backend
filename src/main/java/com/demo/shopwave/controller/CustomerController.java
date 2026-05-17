package com.demo.shopwave.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.shopwave.dto.UserDTO;
import com.demo.shopwave.exception.ShopwaveApplicationException;
import com.demo.shopwave.service.ICustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@RestController
@Tag(name = "Customer Operations", description = "Operations related to Customer management.")
@RequestMapping("/customer")
@Validated
public class CustomerController {

	private ICustomerService customerService;

	@Autowired
	public CustomerController(ICustomerService customerService) {
		this.customerService = customerService;
	}

	@Operation(summary = "Register New Customer", description = "Creates a new customer account with validated input.")
	@PostMapping
	public ResponseEntity<String> registerCustomer(@RequestBody @Valid UserDTO customerDTO)
			throws ShopwaveApplicationException {
		return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(customerDTO));
	}

	@Operation(summary = "Update Customer Name", description = "Updates the customer's name after authenticating via email and password.")
	@PatchMapping
	public ResponseEntity<String> updateCustomerName(
			@RequestParam @Email(message = "Invalid email format") String email,
			@RequestParam @Size(min = 6, message = "Password must be at least 6 characters") String password,
			@RequestParam @Size(min = 1, message = "Name cannot be empty") String name)
			throws ShopwaveApplicationException {

		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomerName(email, password, name));
	}

	@Operation(summary = "Update Customer", description = "Updates the customer after authenticating via email and password.")
	@PutMapping
	public ResponseEntity<String> updateCustomer(@RequestParam @Email(message = "Invalid email format") String email,
			@RequestParam @Size(min = 6, message = "Password must be at least 6 characters") String password,
			@RequestBody @Valid UserDTO customerDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(email, password, customerDTO));
	}

	@Operation(summary = "Delete Customer", description = "Deletes customer record by ID.")
	@DeleteMapping
	public ResponseEntity<String> deleteCustomer(@RequestParam int id) throws ShopwaveApplicationException {

		return ResponseEntity.status(HttpStatus.OK).body(customerService.deleteCustomer(id));
	}

	@Operation(summary = "Get Customer by Email", description = "Retrieves customer details by email.")
	@GetMapping
	public ResponseEntity<UserDTO> getCustomerByEmail(
			@RequestParam @Email(message = "Invalid email format") String email) throws ShopwaveApplicationException {

		return ResponseEntity.status(HttpStatus.OK).body(customerService.getByEmail(email));
	}

	@Operation(summary = "Get All Customers", description = "Retrieves all customer details.")
	@GetMapping("/all")
	public ResponseEntity<java.util.List<UserDTO>> getAllCustomers() throws ShopwaveApplicationException {
		return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
	}
}
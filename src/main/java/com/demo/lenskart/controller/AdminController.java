package com.demo.lenskart.controller;

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

import java.util.List;

import com.demo.lenskart.dto.UserDTO;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.service.IAdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/Admin")
@Validated
@Tag(name = "Admin Controller", description = "Operations related to Admin management")
public class AdminController {

	private IAdminService adminService;

	@Autowired
	public AdminController(IAdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping
	@Operation(summary = "Add a new admin", description = "Registers a new admin with provided details")
	public ResponseEntity<String> addAdmin(@Valid @RequestBody UserDTO adminDTO) throws LenskartApplicationException {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.addAdmin(adminDTO));
	}

	@GetMapping(params = "email")
	@Operation(summary = "Get admin by email", description = "Fetches admin details based on their email")
	public ResponseEntity<UserDTO> getAdminByEmail(@RequestParam @Email(message = "Invalid email format") String email)
			throws LenskartApplicationException {

		return ResponseEntity.status(HttpStatus.OK).body(adminService.getAdminByEmail(email));

	}

	@GetMapping
	@Operation(summary = "Get all admins", description = "Fetches all admins from the system")
	public ResponseEntity<List<UserDTO>> getAllAdmins() throws LenskartApplicationException {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.getAllAdmins());
	}

	@PatchMapping
	@Operation(summary = "Update admin name", description = "Updates admin's name using email and password for authentication")
	public ResponseEntity<String> updateAdminName(@RequestParam @Email(message = "Invalid email format") String email,
			@RequestParam @Size(min = 6, message = "Password must be at least 6 characters") String password,
			@RequestParam @NotBlank(message = "Name cannot be blank") String name) throws LenskartApplicationException {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.updateAdminName(email, password, name));
	}

	@PutMapping
	@Operation(summary = "Update admin", description = "Updates admin using email and password for authentication")
	public ResponseEntity<String> updateAdmin(@RequestParam @Email(message = "Invalid email format") String email,
			@RequestParam @Size(min = 6, message = "Password must be at least 6 characters") String password,
			@Valid @RequestBody UserDTO adminDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.updateAdmin(email, password, adminDTO));
	}

	@DeleteMapping
	@Operation(summary = "Delete admin address", description = "Delete admin's address using credentials")
	public ResponseEntity<String> deleteAdminById(@RequestParam int adminId) throws LenskartApplicationException {
		return ResponseEntity.status(HttpStatus.OK).body(adminService.deleteAdminById(adminId));
	}
}

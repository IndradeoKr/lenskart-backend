package com.demo.shopwave.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO {

	private int userid;

	@NotBlank(message = "Username cannot be empty")
	@Size(min = 4, max = 20, message = "Username must be 4 to 20 characters")
	private String userName;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	private String role;

	@NotBlank(message = "Name cannot be empty")
	private String name;

	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is required")
	private String email;

	@NotNull(message = "Phone number is required")
	@Digits(integer = 10, fraction = 0, message = "Phone number must be 10 digits")
	private Long phoneNumber;

	@NotBlank(message = "Address cannot be empty")
	private String address;
	private String token;

	// Getters and setters
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
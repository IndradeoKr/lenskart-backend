package com.demo.lenskart.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name = "users",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_users_username", columnNames = "userName"),
		@UniqueConstraint(name = "uk_users_email", columnNames = "email"),
		@UniqueConstraint(name = "uk_users_phone_number", columnNames = "phoneNumber")
	}
)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Column(unique = true)
	private String userName;
	private String password;
	private String role;
	private String name;

	@Column(unique = true)
	private String email;
	@Column(unique = true)
	private Long phoneNumber;
	private String address;

	@OneToMany(mappedBy = "customer", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<Cart> carts; // ********************************************************

	@OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "order_id") ///**********************
	private List<Orders> orders;

	public int getUserid() {
		return userId;
	}

	public void setUserid(int userId) {
		this.userId = userId;
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

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + ", role=" + role
				+ ", name=" + name + ", email=" + email + ", number=" + phoneNumber + ", address=" + address
				+ ", carts=" + carts + "]";
	}

}
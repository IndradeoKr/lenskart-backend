package com.demo.shopwave.service;

import com.demo.shopwave.dto.UserDTO;
import com.demo.shopwave.exception.ShopwaveApplicationException;

public interface ICustomerService {
	public String registerCustomer(UserDTO customerDTO) throws ShopwaveApplicationException;

	public String deleteCustomer(int userId) throws ShopwaveApplicationException;

	public UserDTO getByEmail(String email) throws ShopwaveApplicationException;

	public String updateCustomerName(String email, String password, String name) throws ShopwaveApplicationException;

	public String updateCustomer(String email, String password, UserDTO customerDTO)
			throws ShopwaveApplicationException;

	public java.util.List<UserDTO> getAllCustomers() throws ShopwaveApplicationException;
}

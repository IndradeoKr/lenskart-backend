package com.demo.lenskart.service;

import com.demo.lenskart.dto.UserDTO;
import com.demo.lenskart.exception.LenskartApplicationException;

public interface ICustomerService {
	public String registerCustomer(UserDTO customerDTO) throws LenskartApplicationException;

	public String deleteCustomer(int userId) throws LenskartApplicationException;

	public UserDTO getByEmail(String email) throws LenskartApplicationException;

	public String updateCustomerName(String email, String password, String name) throws LenskartApplicationException;

	public String updateCustomer(String email, String password, UserDTO customerDTO)
			throws LenskartApplicationException;

	public java.util.List<UserDTO> getAllCustomers() throws LenskartApplicationException;
}

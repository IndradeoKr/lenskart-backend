package com.demo.shopwave.service;

import java.util.List;

import com.demo.shopwave.dto.UserDTO;
import com.demo.shopwave.exception.ShopwaveApplicationException;

public interface IAdminService {
	public String addAdmin(UserDTO adminDTO) throws ShopwaveApplicationException;

	public UserDTO getAdminByEmail(String email) throws ShopwaveApplicationException;

	public String updateAdminName(String email, String password, String name) throws ShopwaveApplicationException;

	public String deleteAdminById(int adminId) throws ShopwaveApplicationException;

	String updateAdmin(String email, String password, UserDTO adminDTO) throws ShopwaveApplicationException;

	List<UserDTO> getAllAdmins() throws ShopwaveApplicationException;
}

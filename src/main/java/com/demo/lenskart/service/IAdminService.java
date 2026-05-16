package com.demo.lenskart.service;

import java.util.List;

import com.demo.lenskart.dto.UserDTO;
import com.demo.lenskart.exception.LenskartApplicationException;

public interface IAdminService {
	public String addAdmin(UserDTO adminDTO) throws LenskartApplicationException;

	public UserDTO getAdminByEmail(String email) throws LenskartApplicationException;

	public String updateAdminName(String email, String password, String name) throws LenskartApplicationException;

	public String deleteAdminById(int adminId) throws LenskartApplicationException;

	String updateAdmin(String email, String password, UserDTO adminDTO) throws LenskartApplicationException;

	List<UserDTO> getAllAdmins() throws LenskartApplicationException;
}

package com.demo.shopwave.serviceimplement;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.shopwave.dto.UserDTO;
import com.demo.shopwave.entity.User;
import com.demo.shopwave.exception.ShopwaveApplicationException;
import com.demo.shopwave.repository.UserRepository;
import com.demo.shopwave.service.IAdminService;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl implements IAdminService {
	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	static final String ADMIN_ROLE = "ADMIN";
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public AdminServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public String addAdmin(UserDTO adminDTO) throws ShopwaveApplicationException {
		logger.info("Entering addAdmin method with adminDTO: {}", adminDTO);

		// Business rule: Only one admin account is allowed.
		if (!userRepository.findByRoleIgnoreCase(ADMIN_ROLE).isEmpty()) {
			throw new ShopwaveApplicationException("Only one admin is allowed");
		}

		logger.debug("Creating new admin user with username: {}", adminDTO.getUserName());
		User admin = new User();
		logger.info("Admin saved successfully with ID: {}", admin.getUserid());

		admin.setUserName(adminDTO.getUserName());
		admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
		admin.setRole(ADMIN_ROLE);
		admin.setName(adminDTO.getName());
		admin.setEmail(adminDTO.getEmail());
		admin.setPhoneNumber(adminDTO.getPhoneNumber());
		admin.setAddress(adminDTO.getAddress());

		userRepository.save(admin);

		return "Admin added successfully with ID: " + admin.getUserid();
	}

	@Override
	@Transactional
	public String updateAdminName(String email, String password, String name) throws ShopwaveApplicationException {
		logger.info("Entering updateAdminName with email: {}", email);

		User existing = userRepository.findByEmail(email);

		if (existing == null) {
			logger.warn("Admin with email {} not found", email);

			throw new ShopwaveApplicationException("Admin with email " + email + " not found.");
		}
		logger.debug("Found user with ID: {}, Role: {}", existing.getUserid(), existing.getRole());

		if (!(passwordEncoder.matches(password, existing.getPassword()) || existing.getPassword().equals(password))) {
			logger.warn("Incorrect password for admin with email: {}", email);

			throw new ShopwaveApplicationException("Admin with email " + email + " Wrong Password.");
		}

		if (!ADMIN_ROLE.equalsIgnoreCase(existing.getRole())) {
			logger.warn("User with email {} is not an admin. Current role: {}", email, existing.getRole());

			throw new ShopwaveApplicationException("User with email " + email + " is not an admin.");
		}

		existing.setName(name);
		logger.info("Updating admin name for user ID: {} to {}", existing.getUserid(), name);

		return "Admin updated successfully for email: " + email;
	}

	@Override
	@Transactional
	public String updateAdmin(String email, String password, UserDTO adminDTO) {
		logger.info("Entering updateAdmin method with email: {}", email);
		User existing = userRepository.findByEmail(email);

		if (existing == null) {
			logger.warn("No admin found with email: {}", email);
			throw new ShopwaveApplicationException("Admin with email " + email + " not found.");
		}
		logger.debug("Admin found with ID: {}, role: {}", existing.getUserid(), existing.getRole());
		if (!(passwordEncoder.matches(password, existing.getPassword()) || existing.getPassword().equals(password))) {
			logger.warn("Incorrect password attempt for admin with email: {}", email);

			throw new ShopwaveApplicationException("Admin with email " + email + " Wrong Password.");
		}

		if (!ADMIN_ROLE.equalsIgnoreCase(existing.getRole())) {
			logger.warn("User with email {} is not an admin. Current role: {}", email, existing.getRole());
			throw new ShopwaveApplicationException("User with email " + email + " is not an admin.");
		}

		logger.info("Updating admin details for user ID: {}", existing.getUserid());
		existing.setUserName(adminDTO.getUserName());
		existing.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
		existing.setName(adminDTO.getName());
		existing.setEmail(adminDTO.getEmail());
		existing.setPhoneNumber(adminDTO.getPhoneNumber());
		existing.setAddress(adminDTO.getAddress());
		logger.debug("Admin updated with new details");

		return "Admin added successfully with ID: " + existing.getUserid();
	}

	@Override
	public UserDTO getAdminByEmail(String email) throws ShopwaveApplicationException {
		logger.info("Entering getAdminByEmail with email: {}", email);

		User admin = userRepository.findByEmail(email);

		if (admin == null) {
			logger.warn("No user found with email: {}", email);

			throw new ShopwaveApplicationException("Admin with email " + email + " not found.");
		}

		logger.debug("User found with ID: {}, Role: {}", admin.getUserid(), admin.getRole());

		if (!ADMIN_ROLE.equalsIgnoreCase(admin.getRole())) {
			logger.warn("User with email {} is not an admin. Role: {}", email, admin.getRole());

			throw new ShopwaveApplicationException("User with email " + email + " is not an admin.");
		}

		logger.info("Preparing UserDTO for admin with email: {}", email);

		UserDTO adminDTO = new UserDTO();
		adminDTO.setUserid(admin.getUserid());
		adminDTO.setUserName(admin.getUserName());
		adminDTO.setName(admin.getName());
		adminDTO.setAddress(admin.getAddress());
		adminDTO.setEmail(admin.getEmail());
		adminDTO.setPhoneNumber(admin.getPhoneNumber());
		adminDTO.setRole(admin.getRole());
		logger.debug("Returning UserDTO: {}", adminDTO);

		return adminDTO;

	}

	@Override
	@Transactional
	public String deleteAdminById(int adminId) throws ShopwaveApplicationException {
		logger.info("Entering deleteAdminById with adminId: {}", adminId);
		User user = userRepository.findById(adminId).orElse(null);
		if (user == null) {
			logger.warn("No user found with ID: {}", adminId);

			throw new ShopwaveApplicationException("Admin with ID " + adminId + " not found.");
		}
		logger.debug("User found with ID: {}, Role: {}, Email: {}", user.getUserid(), user.getRole(), user.getEmail());

		if (!ADMIN_ROLE.equalsIgnoreCase(user.getRole())) {
			logger.warn("User with ID: {} and email: {} is not an admin. Role: {}", user.getUserid(), user.getEmail(),
					user.getRole());
			throw new ShopwaveApplicationException("User with email " + user.getEmail() + " is not a admin.");
		}
		logger.info("Deleting admin with ID: {}", adminId);

		userRepository.deleteById(adminId);
		return "Admin deleted successfully";
	}

	@Override
	public List<UserDTO> getAllAdmins() throws ShopwaveApplicationException {
		logger.info("Fetching all admins");
		List<User> admins = userRepository.findByRoleIgnoreCase(ADMIN_ROLE);

		List<UserDTO> dtos = new ArrayList<>();
		for (User admin : admins) {
			UserDTO dto = new UserDTO();
			dto.setUserid(admin.getUserid());
			dto.setUserName(admin.getUserName());
			dto.setName(admin.getName());
			dto.setEmail(admin.getEmail());
			dto.setPhoneNumber(admin.getPhoneNumber());
			dto.setAddress(admin.getAddress());
			dto.setRole(admin.getRole());
			dtos.add(dto);
		}
		return dtos;
	}
}

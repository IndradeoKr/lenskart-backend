package com.demo.lenskart.serviceimplement;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.lenskart.dto.UserDTO;
import com.demo.lenskart.entity.User;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.UserRepository;
import com.demo.lenskart.service.ICustomerService;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements ICustomerService {
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	static final String CUSTOMER_ROLE = "CUSTOMER";
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public CustomerServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public String registerCustomer(UserDTO customerDTO) throws LenskartApplicationException {
		logger.info("Entering registerCustomer with customerDTO: {}", customerDTO);

		List<User> existingUsers = userRepository.findAll();
		logger.debug("Retrieved {} users from repository", existingUsers.size());

		// for (User user : existingUsers) {
		// 	logger.debug("Checking user with ID: {}, Role: {}", user.getUserid(), user.getRole());

		// 	if (user.getRole().equals(CUSTOMER_ROLE)) {
		// 		logger.warn("Attempt to register another customer when one already exists. Existing customer ID: {}",
		// 				user.getUserid());
		// 		throw new LenskartApplicationException("Only one customer is allowed");
		// 	}
		// }

		User user = new User();
		user.setUserName(customerDTO.getUserName());
		user.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
		user.setRole(CUSTOMER_ROLE);
		user.setName(customerDTO.getName());
		user.setEmail(customerDTO.getEmail());
		user.setPhoneNumber(customerDTO.getPhoneNumber());
		user.setAddress(customerDTO.getAddress());
		logger.info("Registering new customer with username: {}", user.getUserName());
		userRepository.save(user);
		logger.info("Customer saved successfully with ID: {}", user.getUserid());

		return "Customer registered successfully with ID: " + user.getUserid();
	}

	@Override
	@Transactional
	public String updateCustomer(String email, String password, UserDTO customerDTO) {
		logger.info("Entering updateCustomer with email: {}", email);

		User user = userRepository.findByEmail(email);

		if (user == null) {
			logger.warn("Customer not found with email: {}", email);

			throw new LenskartApplicationException("Customer with email " + email + " not found.");
		}
		logger.debug("Customer found with ID: {}, Role: {}", user.getUserid(), user.getRole());

		if (!(passwordEncoder.matches(password, user.getPassword()) || user.getPassword().equals(password))) {
			logger.warn("Incorrect password for customer with email: {}", email);

			throw new LenskartApplicationException("Customer with email " + email + " Wrong Password.");
		}

		if (!CUSTOMER_ROLE.equalsIgnoreCase(user.getRole())) {
			logger.warn("User with email {} is not a customer. Role found: {}", email, user.getRole());

			throw new LenskartApplicationException("User with email " + email + " is not a customer.");
		}

		logger.info("Updating customer details for ID: {}", user.getUserid());

		user.setUserName(customerDTO.getUserName());
		user.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
		user.setRole(CUSTOMER_ROLE);
		user.setName(customerDTO.getName());
		user.setEmail(customerDTO.getEmail());
		user.setPhoneNumber(customerDTO.getPhoneNumber());
		user.setAddress(customerDTO.getAddress());

		logger.debug("Customer updated with new details: {}", customerDTO);

		return "Customer updated successfully with ID: " + user.getUserid();
	}

	@Override
	@Transactional
	public String updateCustomerName(String email, String password, String name) throws LenskartApplicationException {
		logger.info("Entering updateCustomerName with email: {}", email);

		User user = userRepository.findByEmail(email);

		if (user == null) {
			logger.warn("No customer found with email: {}", email);

			throw new LenskartApplicationException("Customer with email " + email + " not found.");
		}

		logger.debug("User retrieved with ID: {}, Role: {}", user.getUserid(), user.getRole());

		if (!(passwordEncoder.matches(password, user.getPassword()) || user.getPassword().equals(password))) {
			logger.warn("Incorrect password for customer with email: {}", email);

			throw new LenskartApplicationException("Customer with email " + email + " Wrong Password.");
		}

		if (!CUSTOMER_ROLE.equalsIgnoreCase(user.getRole())) {
			logger.warn("User with email {} has role '{}', expected role '{}'", email, user.getRole(), CUSTOMER_ROLE);

			throw new LenskartApplicationException("User with email " + email + " is not a customer.");
		}

		logger.info("Updating customer name for ID: {} to '{}'", user.getUserid(), name);

		user.setName(name);

		return "Customer details updated successfully for email: " + email;
	}

	@Override
	@Transactional
	public String deleteCustomer(int userId) throws LenskartApplicationException {
		logger.info("Entering deleteCustomer with userId: {}", userId);

		User user = userRepository.findById(userId).orElse(null);

		if (user == null) {
			logger.warn("Customer with ID {} not found", userId);

			throw new LenskartApplicationException("Customer with ID " + userId + " not found.");
		}

		logger.debug("Found user with ID: {}, Role: {}, Email: {}", user.getUserid(), user.getRole(), user.getEmail());

		if (!CUSTOMER_ROLE.equalsIgnoreCase(user.getRole())) {
			logger.warn("User with ID: {} and email: {} is not a customer. Role found: {}", user.getUserid(),
					user.getEmail(), user.getRole());

			throw new LenskartApplicationException("User with email " + user.getEmail() + " is not a customer.");
		}

		logger.info("Deleting customer with ID: {}", userId);

		userRepository.deleteById(userId);
		return "Customer with ID " + userId + " deleted successfully.";
	}

	@Override
	public UserDTO getByEmail(String email) throws LenskartApplicationException {
		logger.info("Entering getByEmail method with email: {}", email);

		User user = userRepository.findByEmail(email);

		if (user == null) {
			logger.warn("Customer not found with email: {}", email);

			throw new LenskartApplicationException("Customer with email " + email + " not found.");
		}

		logger.debug("User retrieved: ID: {}, Role: {}, Name: {}", user.getUserid(), user.getRole(), user.getName());

		if (!CUSTOMER_ROLE.equalsIgnoreCase(user.getRole())) {
			logger.warn("User with email {} is not a customer. Role found: {}", email, user.getRole());

			throw new LenskartApplicationException("User with email " + email + " is not a customer.");
		}

		logger.info("Transforming User entity to UserDTO for email: {}", email);

		UserDTO userDTO = new UserDTO();
		userDTO.setUserid(user.getUserid());
		userDTO.setName(user.getName());
		userDTO.setAddress(user.getAddress());
		userDTO.setEmail(user.getEmail());
		userDTO.setPhoneNumber(user.getPhoneNumber());
		userDTO.setRole(user.getRole());
		logger.debug("Returning UserDTO: {}", userDTO);

		return userDTO;

	}

	@Override
	public List<UserDTO> getAllCustomers() throws LenskartApplicationException {
		logger.info("Entering getAllCustomers method");
		List<User> users = userRepository.findAll();
		return users.stream()
				.filter(user -> CUSTOMER_ROLE.equalsIgnoreCase(user.getRole()))
				.map(user -> {
					UserDTO userDTO = new UserDTO();
					userDTO.setUserid(user.getUserid());
					userDTO.setName(user.getName());
					userDTO.setAddress(user.getAddress());
					userDTO.setEmail(user.getEmail());
					userDTO.setPhoneNumber(user.getPhoneNumber());
					userDTO.setRole(user.getRole());
					userDTO.setUserName(user.getUserName());
					return userDTO;
				})
				.toList();
	}
}

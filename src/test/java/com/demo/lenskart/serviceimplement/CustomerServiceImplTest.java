package com.demo.lenskart.serviceimplement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.lenskart.dto.UserDTO;
import com.demo.lenskart.entity.User;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
	@InjectMocks
	private CustomerServiceImpl customerService;

	@Mock
	private UserRepository userRepository;

	private User user;
	private UserDTO userDTO;

	@BeforeEach
	void init() {
		user = new User();
		user.setUserName("abc_123");
		user.setPassword("000000");
		user.setRole("CUSTOMER");
		user.setName("abc");
		user.setEmail("abc@gmail.com");
		user.setPhoneNumber(7366813330l);
		user.setAddress("Gaya");

		userDTO = new UserDTO();
		userDTO.setUserName("abc_123");
		userDTO.setPassword("000000");
		userDTO.setRole("CUSTOMER");
		userDTO.setName("abc");
		userDTO.setEmail("abc@gmail.com");
		userDTO.setPhoneNumber(7366813330l);
		userDTO.setAddress("Gaya");
	}

	@Test
	void testRegisterCustomer_SuccessfulAdded() throws LenskartApplicationException {
		List<User> listOfUsers = new ArrayList<>();
		when(userRepository.findAll()).thenReturn(listOfUsers);
		when(userRepository.save(any(User.class))).thenReturn(user);
		String result = customerService.registerCustomer(userDTO);
		assertEquals("Customer registered successfully with ID: " + user.getUserid(), result);
		verify(userRepository).findAll();
		verify(userRepository).save(any(User.class));
	}

	@Test
	void testRegisterCustomer_OnlyOneCustomerAllowed() throws LenskartApplicationException {
		List<User> listOfUsers = new ArrayList<>();
		listOfUsers.add(user);
		when(userRepository.findAll()).thenReturn(listOfUsers);
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> customerService.registerCustomer(userDTO));

		assertEquals("Only one customer is allowed", ex.getMessage());
	}

	@Test
	void testUpdateCustomerName_SuccessfulUpdate() throws LenskartApplicationException
	{
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(user);
		String result=customerService.updateCustomerName("abc@gmail.com", "000000", "Dev");
		assertEquals("Customer details updated successfully for email: abc@gmail.com", result);
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
    void testUpdateCustomerName_AdminNotFound() throws LenskartApplicationException{
        when(userRepository.findByEmail("abc@gmail.com")).thenReturn(null);

        LenskartApplicationException ex = assertThrows(
            LenskartApplicationException.class,
            () ->customerService.updateCustomerName("abc@gmail.com", "000000", "Dev")
        );

        assertEquals("Customer with email abc@gmail.com not found.", ex.getMessage());
        verify(userRepository).findByEmail("abc@gmail.com");
    }

	@Test 
	void testUpdateCustomerName_WrongPassword() throws LenskartApplicationException{
        when(userRepository.findByEmail("abc@gmail.com")).thenReturn(user);

        LenskartApplicationException ex = assertThrows(
            LenskartApplicationException.class,
            () -> customerService.updateCustomerName("abc@gmail.com", "123456", "Dev")
        );

        assertEquals("Customer with email abc@gmail.com Wrong Password.", ex.getMessage());
        verify(userRepository).findByEmail("abc@gmail.com");
    }

	@Test
	void testUpdateCustomerName_UserIsNotAdmin() throws LenskartApplicationException {
		user.setRole("ADMIN");
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(user);

		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> customerService.updateCustomerName("abc@gmail.com", "000000", "Dev"));

		assertEquals("User with email abc@gmail.com is not a customer.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testUpdateCustomer_SuccefulUpdate() throws LenskartApplicationException
	{
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(user);
		String result=customerService.updateCustomer("abc@gmail.com", "000000", userDTO);
		assertEquals("Customer updated successfully with ID: " + user.getUserid(), result);
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testUpdateCustomer_AdminNotFound() throws LenskartApplicationException
	{
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(null);
		LenskartApplicationException ex=assertThrows(
			LenskartApplicationException.class,
				()->customerService.updateCustomer("abc@gmail.com", "000000", userDTO)
		);
		assertEquals("Customer with email abc@gmail.com not found.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testUpdateCustomer_WrongPassword() throws LenskartApplicationException
	{
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(user);
		LenskartApplicationException ex=assertThrows(
			LenskartApplicationException.class,
				()->customerService.updateCustomer("abc@gmail.com", "123456", userDTO)
		);
		assertEquals("Customer with email abc@gmail.com Wrong Password.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testUpdateCustomer_UserIsNotAdmin() throws LenskartApplicationException {
		user.setRole("ADMIN");
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(user);
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> customerService.updateCustomer("abc@gmail.com", "000000", userDTO));
		assertEquals("User with email abc@gmail.com is not a customer.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testGetByEmail_successfulGet() throws LenskartApplicationException {

		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(user);
		UserDTO result = customerService.getByEmail("abc@gmail.com");

		assertEquals(userDTO.getUserid(), result.getUserid());
		assertEquals(userDTO.getName(), result.getName());
		assertEquals(userDTO.getAddress(), result.getAddress());
		assertEquals(userDTO.getEmail(), result.getEmail());
		assertEquals(userDTO.getPhoneNumber(), result.getPhoneNumber());
		assertEquals(userDTO.getRole(), result.getRole());

		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testGetByEmail_AdminNotFound() throws LenskartApplicationException {
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(null);
		LenskartApplicationException ex=assertThrows(LenskartApplicationException.class,
				()->customerService.getByEmail("abc@gmail.com"));
		assertEquals("Customer with email abc@gmail.com not found.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testGetByEmail_UserIsNotCustomer() throws LenskartApplicationException {
		user.setRole("ADMIN");
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(user);
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> customerService.getByEmail("abc@gmail.com"));
		assertEquals("User with email abc@gmail.com is not a customer.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testDeleteCustomer_SuccessfulDelete() throws LenskartApplicationException {
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		String result = customerService.deleteCustomer(1);
		assertEquals("Customer with ID 1 deleted successfully.", result);
		verify(userRepository).deleteById(1);
		verify(userRepository).findById(1);
	}

	@Test
	void testDeleteCustomer_CustomerNotFound() throws LenskartApplicationException {
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> customerService.deleteCustomer(1));
		assertEquals("Customer with ID 1 not found.", ex.getMessage());
		verify(userRepository).findById(1);
	}

	@Test
	void testDeleteCustomer_UserIsNotCustomer() throws LenskartApplicationException {
		user.setRole("Admin");
		when(userRepository.findById(1)).thenReturn(Optional.of(user));
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> customerService.deleteCustomer(1));
		assertEquals("User with email abc@gmail.com is not a customer.", ex.getMessage());
		verify(userRepository).findById(1);
	}
}

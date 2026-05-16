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
class AdminServiceImplTest {
	@InjectMocks
	private AdminServiceImpl adminService;

	@Mock
	private UserRepository userRepository;

	private User admin;
	private UserDTO adminDTO;

	@BeforeEach
	void init() {
		admin = new User();
		admin.setUserName("abc_123");
		admin.setPassword("000000");
		admin.setRole("ADMIN");
		admin.setName("abc");
		admin.setEmail("abc@gmail.com");
		admin.setPhoneNumber(7366813330l);
		admin.setAddress("Gaya");

		adminDTO = new UserDTO();
		adminDTO.setUserName("abc_123");
		adminDTO.setPassword("000000");
		adminDTO.setRole("ADMIN");
		adminDTO.setName("abc");
		adminDTO.setEmail("abc@gmail.com");
		adminDTO.setPhoneNumber(7366813330l);
		adminDTO.setAddress("Gaya");
	}

	@Test
	void testAddAdmin_SuccessfulAdded() throws LenskartApplicationException {
		List<User> listOfUsers = new ArrayList<>();
		when(userRepository.findAll()).thenReturn(listOfUsers);
		when(userRepository.save(any(User.class))).thenReturn(admin);
		String result = adminService.addAdmin(adminDTO);
		assertEquals("Admin added successfully with ID: " + admin.getUserid(), result);
		verify(userRepository).findAll();
		verify(userRepository).save(any(User.class));
	}

	@Test
	void testAddAdmin_OnlyOneAdminAllowed() throws LenskartApplicationException {
		List<User> listOfUsers = new ArrayList<>();
		listOfUsers.add(admin);
		when(userRepository.findAll()).thenReturn(listOfUsers);
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> adminService.addAdmin(adminDTO));

		assertEquals("Only one admin is allowed", ex.getMessage());
	}

	@Test
	void testUpdateAdminName_SuccessfulUpdate() throws LenskartApplicationException
	{
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(admin);
		String result=adminService.updateAdminName("abc@gmail.com", "000000", "Dev");
		assertEquals("Admin updated successfully for email: abc@gmail.com", result);
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
    void testUpdateAdminName_AdminNotFound() throws LenskartApplicationException{
        when(userRepository.findByEmail("abc@gmail.com")).thenReturn(null);

        LenskartApplicationException ex = assertThrows(
            LenskartApplicationException.class,
            () -> adminService.updateAdminName("abc@gmail.com", "000000", "Dev")
        );

        assertEquals("Admin with email abc@gmail.com not found.", ex.getMessage());
        verify(userRepository).findByEmail("abc@gmail.com");
    }

	@Test 
	void testUpdateAdminName_WrongPassword() throws LenskartApplicationException{
        when(userRepository.findByEmail("abc@gmail.com")).thenReturn(admin);

        LenskartApplicationException ex = assertThrows(
            LenskartApplicationException.class,
            () -> adminService.updateAdminName("abc@gmail.com", "123456", "Dev")
        );

        assertEquals("Admin with email abc@gmail.com Wrong Password.", ex.getMessage());
        verify(userRepository).findByEmail("abc@gmail.com");
    }

	@Test
	void testUpdateAdminName_UserIsNotAdmin() throws LenskartApplicationException {
		admin.setRole("CUSTOMER");
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(admin);

		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> adminService.updateAdminName("abc@gmail.com", "000000", "Dev"));

		assertEquals("User with email abc@gmail.com is not an admin.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testUpdateAdmin_SuccefulUpdate() throws LenskartApplicationException
	{
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(admin);
		String result=adminService.updateAdmin("abc@gmail.com", "000000", adminDTO);
		assertEquals("Admin added successfully with ID: " + admin.getUserid(), result);
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testUpdateAdmin_AdminNotFound() throws LenskartApplicationException
	{
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(null);
		LenskartApplicationException ex=assertThrows(
			LenskartApplicationException.class,
				()->adminService.updateAdmin("abc@gmail.com", "000000", adminDTO)
		);
		assertEquals("Admin with email abc@gmail.com not found.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testUpdateAdmin_WrongPassword() throws LenskartApplicationException
	{
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(admin);
		LenskartApplicationException ex=assertThrows(
			LenskartApplicationException.class,
				()->adminService.updateAdmin("abc@gmail.com", "123456", adminDTO)
		);
		assertEquals("Admin with email abc@gmail.com Wrong Password.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testUpdateAdmin_UserIsNotAdmin() throws LenskartApplicationException {
		admin.setRole("CUSTOMER");
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(admin);
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> adminService.updateAdmin("abc@gmail.com", "000000", adminDTO));
		assertEquals("User with email abc@gmail.com is not an admin.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testGetAdminByEmail_successfulGet() throws LenskartApplicationException {
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(admin);
		UserDTO result = adminService.getAdminByEmail("abc@gmail.com");

		assertEquals(adminDTO.getUserid(), result.getUserid());
		assertEquals(adminDTO.getName(), result.getName());
		assertEquals(adminDTO.getAddress(), result.getAddress());
		assertEquals(adminDTO.getEmail(), result.getEmail());
		assertEquals(adminDTO.getPhoneNumber(), result.getPhoneNumber());
		assertEquals(adminDTO.getRole(), result.getRole());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testGetAdminByEmail_AdminNotFound() throws LenskartApplicationException {
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(null);
		LenskartApplicationException ex=assertThrows(LenskartApplicationException.class,
				()->adminService.getAdminByEmail("abc@gmail.com"));
		assertEquals("Admin with email abc@gmail.com not found.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testGetAdminByEmail_UserIsNotAdmin() throws LenskartApplicationException {
		admin.setRole("CUSTOMER");
		when(userRepository.findByEmail("abc@gmail.com")).thenReturn(admin);
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> adminService.getAdminByEmail("abc@gmail.com"));
		assertEquals("User with email abc@gmail.com is not an admin.", ex.getMessage());
		verify(userRepository).findByEmail("abc@gmail.com");
	}

	@Test
	void testDeleteAdminById_SuccessfulDelete() throws LenskartApplicationException {
		when(userRepository.findById(1)).thenReturn(Optional.of(admin));
		String result = adminService.deleteAdminById(1);
		assertEquals("Admin deleted successfully", result);
		verify(userRepository).deleteById(1);
		verify(userRepository).findById(1);
	}

	@Test
	void testDeleteAdminById_AdminNotFound() throws LenskartApplicationException {
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		LenskartApplicationException ex=assertThrows(LenskartApplicationException.class,
				()->adminService.deleteAdminById(1));
		assertEquals("Admin with ID 1 not found.", ex.getMessage());
		verify(userRepository).findById(1);
	}

	@Test
	void testDeleteAdminById_UserIsNotAdmin() throws LenskartApplicationException {
		admin.setRole("CUSTOMER");
		when(userRepository.findById(1)).thenReturn(Optional.of(admin));
		LenskartApplicationException ex = assertThrows(LenskartApplicationException.class,
				() -> adminService.deleteAdminById(1));
		assertEquals("User with email abc@gmail.com is not a admin.", ex.getMessage());
		verify(userRepository).findById(1);
	}

}

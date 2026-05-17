package com.demo.shopwave.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.shopwave.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);

	boolean existsByEmail(String email);

	List<User> findByRoleIgnoreCase(String role);
}

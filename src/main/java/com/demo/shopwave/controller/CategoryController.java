package com.demo.shopwave.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.demo.shopwave.dto.CategoryDTO;
import com.demo.shopwave.service.ICategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/category")
@Tag(name = "Category Operations", description = "Endpoints for managing a Category, including creation, updates, deletion, and retrieval.")
public class CategoryController {
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	private ICategoryService categoryService;

	@Autowired
	public CategoryController(ICategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Operation(summary = "Add a New Category", description = "Adds a new Category to the catalog.")
	@PostMapping
	public ResponseEntity<String> addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
		logger.info("Received request to add category: {}", categoryDTO.getCategoryName());
		String categoryCreatedMessage = categoryService.addCategory(categoryDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryCreatedMessage);
	}

	@Operation(summary = "updates a New Category", description = "Updating a new Category to the catalog.")
	@PutMapping("/{id}/{name}")
	public ResponseEntity<String> updateCategory(@PathVariable int id, @PathVariable String name) {
		logger.info("Received request to update category ID {} with new name '{}'", id, name);
		String categoryupdatedMessage = categoryService.updateCategory(id, name);
		return ResponseEntity.status(HttpStatus.OK).body(categoryupdatedMessage);
	}

	@Operation(summary = "Delete a Category", description = "Deletes a Category from the catalog using its ID. Returns confirmation upon success.")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> removeCategory(@PathVariable int id) {
		logger.info("Received request to delete category with ID: {}", id);
		String categoryremoveMessage = categoryService.removeCategory(id);
		return ResponseEntity.status(HttpStatus.OK).body(categoryremoveMessage);
	}

	@Operation(summary = "Retrieve Category by Name", description = "Fetches the Category under a specified  Name.")
	@GetMapping("/name/{name}")
	public CategoryDTO getByName(@PathVariable String name) {
		logger.info("Fetching category by name: {}", name);
		return categoryService.searchCategoryByName(name);
	}

	@Operation(summary = "Retrieve Category by id", description = "Fetches the Category under a specified id.")
	@GetMapping("/id/{id}")
	public CategoryDTO getById(@PathVariable int id) {
		logger.info("Fetching category by ID: {}", id);
		return categoryService.searchCategoryById(id);
	}

	@Operation(summary = "Retrieve all categories", description = "Fetches all categories from the system")
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> getAllCategories() {
		logger.info("Received request to fetch all categories");
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAllCategories());
	}
}

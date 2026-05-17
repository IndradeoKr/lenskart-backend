package com.demo.shopwave.serviceimplement;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.shopwave.dto.CategoryDTO;
import com.demo.shopwave.entity.Category;
import com.demo.shopwave.exception.ShopwaveApplicationException;
import com.demo.shopwave.repository.CategoryRepository;
import com.demo.shopwave.service.ICategoryService;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements ICategoryService {
	private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional
	public String addCategory(CategoryDTO categoryDTO) throws ShopwaveApplicationException {
		logger.debug("Processing category addition: {}", categoryDTO.getCategoryName());

		Category category = new Category();
		if (categoryDTO.getCategoryName() == null || categoryDTO.getCategoryName().trim().isEmpty()) {
			logger.error("Invalid category data: {}", categoryDTO);
			throw new ShopwaveApplicationException("Category name must not be empty");
		}

		if (categoryRepository.findByCategoryName(categoryDTO.getCategoryName().trim()) != null) {
			throw new ShopwaveApplicationException("Category is already present");
		}

		category.setCategoryName(categoryDTO.getCategoryName());
		categoryRepository.save(category);
		logger.info("Category added with ID: {}", category.getCategoryId());
		return "Category added successfully with ID: " + category.getCategoryId();

	}

	@Override
	@Transactional
	public String removeCategory(int categoryId) throws ShopwaveApplicationException {
		logger.debug("Attempting to delete category with ID: {}", categoryId);

		Category category = categoryRepository.findById(categoryId).orElse(null);
		if (category == null) {
			throw new ShopwaveApplicationException("Category not found with ID: " + categoryId);
		}

		// With Category.products configured as cascade + orphanRemoval, deleting a category
		// will also delete products in that category.
		categoryRepository.delete(category);
		logger.info("Category deleted successfully — ID: {}", categoryId);

		return "Category removed successfully";

	}

	@Override
	@Transactional
	public String updateCategory(int categoryId, String categoryName) throws ShopwaveApplicationException {
		logger.debug("Updating category with ID: {}, new name: {}", categoryId, categoryName);

		if (categoryName == null || categoryName.trim().isEmpty()) {
			logger.warn("Provided category name is empty");
			throw new ShopwaveApplicationException("New category name must not be empty");
		}
		Category category = categoryRepository.findById(categoryId).orElse(null);
		if (category == null) {
			logger.error("Category not found — cannot update ID: {}", categoryId);
			throw new ShopwaveApplicationException("Cannot update. Category not found with ID: " + categoryId);
		}

		category.setCategoryName(categoryName.trim()); // Placeholder: real update would use a DTO
		categoryRepository.save(category);

		logger.info("Category updated successfully for ID: {}", categoryId);
		return "Category updated successfully for ID: " + categoryId;

	}

	@Override
	public CategoryDTO searchCategoryByName(String name) throws ShopwaveApplicationException {
		logger.debug("Searching category by name: {}", name);

		if (name == null || name.trim().isEmpty()) {
			logger.warn("Category name input is empty");
			throw new ShopwaveApplicationException("Category name must not be empty");
		}

		Category category = categoryRepository.findByCategoryName(name);
		if (category == null) {
			logger.error("No category found with name: {}", name);
			throw new ShopwaveApplicationException("No category found with name: " + name);

		}
		logger.info("Category found for name '{}', ID: {}", name, category.getCategoryId());
		CategoryDTO dto = new CategoryDTO();
		dto.setCategoryId(category.getCategoryId());
		dto.setCategoryName(category.getCategoryName());
		return dto;
	}

	@Override
	public CategoryDTO searchCategoryById(int id) throws ShopwaveApplicationException {
		logger.debug("Searching category by ID: {}", id);

		Category category = categoryRepository.findById(id).orElse(null);
		if (category == null) {
			logger.error("No category found with ID: {}", id);
			throw new ShopwaveApplicationException("No category found with ID: " + id);

		}
		logger.info("Category found for ID: {} with name '{}'", id, category.getCategoryName());

		CategoryDTO dto = new CategoryDTO();
		dto.setCategoryId(category.getCategoryId());
		dto.setCategoryName(category.getCategoryName());
		return dto;
	}

	@Override
	public List<CategoryDTO> findAllCategories() throws ShopwaveApplicationException {
		logger.debug("Fetching all categories");

		List<Category> categories = categoryRepository.findAll();
		if (categories.isEmpty()) {
			throw new ShopwaveApplicationException("No categories available in the database");
		}

		List<CategoryDTO> dtos = new ArrayList<>();
		for (Category c : categories) {
			CategoryDTO dto = new CategoryDTO();
			dto.setCategoryId(c.getCategoryId());
			dto.setCategoryName(c.getCategoryName());
			dtos.add(dto);
		}

		return dtos;
	}
}

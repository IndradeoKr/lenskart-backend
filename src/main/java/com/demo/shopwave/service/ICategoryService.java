package com.demo.shopwave.service;

import java.util.List;

import com.demo.shopwave.dto.CategoryDTO;
import com.demo.shopwave.exception.ShopwaveApplicationException;

public interface ICategoryService {
	public String addCategory(CategoryDTO category) throws ShopwaveApplicationException;

	public String removeCategory(int categoryId) throws ShopwaveApplicationException;

	public String updateCategory(int categoryId, String categoryName) throws ShopwaveApplicationException;

	public CategoryDTO searchCategoryByName(String name) throws ShopwaveApplicationException;

	public CategoryDTO searchCategoryById(int id) throws ShopwaveApplicationException;

	public List<CategoryDTO> findAllCategories() throws ShopwaveApplicationException;

}

package com.demo.lenskart.service;

import java.util.List;

import com.demo.lenskart.dto.CategoryDTO;
import com.demo.lenskart.exception.LenskartApplicationException;

public interface ICategoryService {
	public String addCategory(CategoryDTO category) throws LenskartApplicationException;

	public String removeCategory(int categoryId) throws LenskartApplicationException;

	public String updateCategory(int categoryId, String categoryName) throws LenskartApplicationException;

	public CategoryDTO searchCategoryByName(String name) throws LenskartApplicationException;

	public CategoryDTO searchCategoryById(int id) throws LenskartApplicationException;

	public List<CategoryDTO> findAllCategories() throws LenskartApplicationException;

}

package com.demo.shopwave.serviceimplement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.shopwave.dto.CategoryDTO;
import com.demo.shopwave.entity.Category;
import com.demo.shopwave.exception.ShopwaveApplicationException;
import com.demo.shopwave.repository.CategoryRepository;

class CategoryServiceImplTest {
	@InjectMocks
	private CategoryServiceImpl categoryService;

	@Mock
	private CategoryRepository categoryRepository;

	private Category category;
	private CategoryDTO categoryDTO;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryName("Eyewear");

		category = new Category();
		category.setCategoryId(1);
		category.setCategoryName("Eyewear");
	}

	/* Positive Test Cases */
	@Test
	    void testAddCategory_Success() throws ShopwaveApplicationException {
	        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
	        when(categoryRepository.save(any(Category.class))).thenReturn(category);

	        String result = categoryService.addCategory(categoryDTO);
	        assertTrue(result.contains("Category added successfully"));
	    }

	@Test
	    void testRemoveCategory_Success() throws ShopwaveApplicationException {
	        when(categoryRepository.existsById(1)).thenReturn(true);
	        doNothing().when(categoryRepository).deleteById(1);

	        String result = categoryService.removeCategory(1);
	        assertEquals("Category removed successfully", result);
	    }

	@Test
	    void testUpdateCategory_Success() throws ShopwaveApplicationException {
	        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
	        when(categoryRepository.save(any(Category.class))).thenReturn(category);

	        String result = categoryService.updateCategory(1, "New Eyewear");
	        assertTrue(result.contains("Category updated successfully"));
	    }

	@Test
	    void testSearchCategoryByName_Success() throws ShopwaveApplicationException {
	        when(categoryRepository.findByCategoryName("Eyewear")).thenReturn(category);

	        CategoryDTO result = categoryService.searchCategoryByName("Eyewear");
	        assertEquals("Eyewear", result.getCategoryName());
	    }

	@Test
	    void testSearchCategoryById_Success() throws ShopwaveApplicationException {
	        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

	        CategoryDTO result = categoryService.searchCategoryById(1);
	        assertEquals(1, result.getCategoryId());
	    }

	/* Negative Test Cases */

	@Test
	    void testAddCategory_AlreadyExists() {
	        when(categoryRepository.findAll()).thenReturn(List.of(category));
	        assertThrows(ShopwaveApplicationException.class, () -> categoryService.addCategory(categoryDTO));
	    }

	@Test
	void testAddCategory_InvalidName() {
		categoryDTO.setCategoryName(null);
		assertThrows(ShopwaveApplicationException.class, () -> categoryService.addCategory(categoryDTO));
	}

	@Test
	    void testRemoveCategory_NotFound() {
	        when(categoryRepository.existsById(1)).thenReturn(false);
	        assertThrows(ShopwaveApplicationException.class, () -> categoryService.removeCategory(1));
	    }

	@Test
	void testUpdateCategory_EmptyName() {
		assertThrows(ShopwaveApplicationException.class, () -> categoryService.updateCategory(1, ""));
	}

	@Test
	    void testUpdateCategory_NotFound() {
	        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
	        assertThrows(ShopwaveApplicationException.class, () -> categoryService.updateCategory(1, "New Name"));
	    }

	@Test
	void testSearchCategoryByName_EmptyName() {
		assertThrows(ShopwaveApplicationException.class, () -> categoryService.searchCategoryByName(""));
	}

	@Test
	    void testSearchCategoryByName_NotFound() {
	        when(categoryRepository.findByCategoryName("Eyewear")).thenReturn(null);
	        assertThrows(ShopwaveApplicationException.class, () -> categoryService.searchCategoryByName("Eyewear"));
	    }

	@Test
	    void testSearchCategoryById_NotFound() {
	        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
	        assertThrows(ShopwaveApplicationException.class, () -> categoryService.searchCategoryById(1));
	    }

}

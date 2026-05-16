package com.demo.lenskart.serviceimplement;

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

import com.demo.lenskart.dto.CategoryDTO;
import com.demo.lenskart.entity.Category;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.CategoryRepository;

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
	    void testAddCategory_Success() throws LenskartApplicationException {
	        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());
	        when(categoryRepository.save(any(Category.class))).thenReturn(category);

	        String result = categoryService.addCategory(categoryDTO);
	        assertTrue(result.contains("Category added successfully"));
	    }

	@Test
	    void testRemoveCategory_Success() throws LenskartApplicationException {
	        when(categoryRepository.existsById(1)).thenReturn(true);
	        doNothing().when(categoryRepository).deleteById(1);

	        String result = categoryService.removeCategory(1);
	        assertEquals("Category removed successfully", result);
	    }

	@Test
	    void testUpdateCategory_Success() throws LenskartApplicationException {
	        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
	        when(categoryRepository.save(any(Category.class))).thenReturn(category);

	        String result = categoryService.updateCategory(1, "New Eyewear");
	        assertTrue(result.contains("Category updated successfully"));
	    }

	@Test
	    void testSearchCategoryByName_Success() throws LenskartApplicationException {
	        when(categoryRepository.findByCategoryName("Eyewear")).thenReturn(category);

	        CategoryDTO result = categoryService.searchCategoryByName("Eyewear");
	        assertEquals("Eyewear", result.getCategoryName());
	    }

	@Test
	    void testSearchCategoryById_Success() throws LenskartApplicationException {
	        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

	        CategoryDTO result = categoryService.searchCategoryById(1);
	        assertEquals(1, result.getCategoryId());
	    }

	/* Negative Test Cases */

	@Test
	    void testAddCategory_AlreadyExists() {
	        when(categoryRepository.findAll()).thenReturn(List.of(category));
	        assertThrows(LenskartApplicationException.class, () -> categoryService.addCategory(categoryDTO));
	    }

	@Test
	void testAddCategory_InvalidName() {
		categoryDTO.setCategoryName(null);
		assertThrows(LenskartApplicationException.class, () -> categoryService.addCategory(categoryDTO));
	}

	@Test
	    void testRemoveCategory_NotFound() {
	        when(categoryRepository.existsById(1)).thenReturn(false);
	        assertThrows(LenskartApplicationException.class, () -> categoryService.removeCategory(1));
	    }

	@Test
	void testUpdateCategory_EmptyName() {
		assertThrows(LenskartApplicationException.class, () -> categoryService.updateCategory(1, ""));
	}

	@Test
	    void testUpdateCategory_NotFound() {
	        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
	        assertThrows(LenskartApplicationException.class, () -> categoryService.updateCategory(1, "New Name"));
	    }

	@Test
	void testSearchCategoryByName_EmptyName() {
		assertThrows(LenskartApplicationException.class, () -> categoryService.searchCategoryByName(""));
	}

	@Test
	    void testSearchCategoryByName_NotFound() {
	        when(categoryRepository.findByCategoryName("Eyewear")).thenReturn(null);
	        assertThrows(LenskartApplicationException.class, () -> categoryService.searchCategoryByName("Eyewear"));
	    }

	@Test
	    void testSearchCategoryById_NotFound() {
	        when(categoryRepository.findById(1)).thenReturn(Optional.empty());
	        assertThrows(LenskartApplicationException.class, () -> categoryService.searchCategoryById(1));
	    }

}

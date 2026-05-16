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

import com.demo.lenskart.dto.ProductDTO;
import com.demo.lenskart.entity.Category;
import com.demo.lenskart.entity.Product;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.CategoryRepository;
import com.demo.lenskart.repository.ProductRepository;

class ProductServiceImplTest {
	@InjectMocks
	private ProductServiceImpl productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private CategoryRepository categoryRepository;

	private ProductDTO productDTO;
	private Product product;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		productDTO = new ProductDTO();
		productDTO.setProductId(1);
		productDTO.setProductName("Sunglasses");
		productDTO.setProductPrice(1200.0);
		productDTO.setQuantity(5);
		productDTO.setBrand("RayBan");
		productDTO.setCategory("Eyewear");

		product = new Product();
		product.setProductId(1);
		product.setProductName("Sunglasses");
		product.setProductPrice(1200.0);
		product.setQuantity(5);
		product.setBrand("RayBan");

		Category category = new Category();
		category.setCategoryName("Eyewear");
		product.setCategory(category);
	}
	/* Positive Test Cases */

	@Test
    void testAddProduct_Success() throws LenskartApplicationException {
        when(categoryRepository.findByCategoryName("Eyewear")).thenReturn(new Category());
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        String response = productService.addProduct(productDTO);
        assertTrue(response.contains("Product added successfully"));
    }

	@Test
    void testUpdateProduct_Success() throws LenskartApplicationException {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        String result = productService.updateProduct(productDTO);
        assertTrue(result.contains("updated successfully"));
    }

	@Test
    void testDeleteProduct_Success() throws LenskartApplicationException {
        when(productRepository.existsById(1)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1);

        String result = productService.deleteProduct(1);
        assertTrue(result.contains("deleted successfully"));
    }

	@Test
    void testGetById_Success() throws LenskartApplicationException {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getById(1);
        assertEquals("Sunglasses", result.getProductName());
    }

	@Test
    void testGetProductByBrand_Success() throws LenskartApplicationException {
        when(productRepository.findByBrand("RayBan")).thenReturn(List.of(product));

        List<ProductDTO> result = productService.getProductByBrand("RayBan");
        assertEquals(1, result.size());
    }

	@Test
    void testFindAll_Success() throws LenskartApplicationException {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDTO> result = productService.findAll();
        assertEquals(1, result.size());
    }
	/* Negative Test Cases */

	@Test
	void testAddProduct_InvalidData() {
		productDTO.setProductName(null);
		assertThrows(LenskartApplicationException.class, () -> productService.addProduct(productDTO));
	}

	@Test
    void testAddProduct_CategoryNotFound() {
        when(categoryRepository.findByCategoryName("Eyewear")).thenReturn(null);
        assertThrows(LenskartApplicationException.class, () -> productService.addProduct(productDTO));
    }

	@Test
    void testAddProduct_AlreadyExists() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        assertThrows(LenskartApplicationException.class, () -> productService.addProduct(productDTO));
    }

	@Test
	void testUpdateProduct_InvalidId() {
		productDTO.setProductId(0);
		assertThrows(LenskartApplicationException.class, () -> productService.updateProduct(productDTO));
	}

	@Test
    void testUpdateProduct_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(LenskartApplicationException.class, () -> productService.updateProduct(productDTO));
    }

	@Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1)).thenReturn(false);
        assertThrows(LenskartApplicationException.class, () -> productService.deleteProduct(1));
    }

	@Test
    void testGetById_NotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(LenskartApplicationException.class, () -> productService.getById(1));
    }

	@Test
	void testGetProductByBrand_EmptyName() {
		assertThrows(LenskartApplicationException.class, () -> productService.getProductByBrand(""));
	}

	@Test
    void testGetProductByBrand_NotFound() {
        when(productRepository.findByBrand("RayBan")).thenReturn(Collections.emptyList());
        assertThrows(LenskartApplicationException.class, () -> productService.getProductByBrand("RayBan"));
    }

	@Test
    void testFindAll_EmptyDatabase() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(LenskartApplicationException.class, () -> productService.findAll());
    }

}

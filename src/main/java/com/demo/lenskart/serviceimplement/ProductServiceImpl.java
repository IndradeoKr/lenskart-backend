package com.demo.lenskart.serviceimplement;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.lenskart.dto.ProductDTO;
import com.demo.lenskart.entity.Product;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.CategoryRepository;
import com.demo.lenskart.repository.ProductRepository;
import com.demo.lenskart.service.IProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements IProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	private ProductRepository productRepository;

	private CategoryRepository categoryRepository;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	@Transactional
	public String addProduct(ProductDTO productDTO) throws LenskartApplicationException {
		logger.debug("Initiating product add operation for: {}", productDTO);
		if (productDTO == null || productDTO.getProductName() == null || productDTO.getProductPrice() <= 0
				|| productDTO.getQuantity() < 0 || productDTO.getBrand() == null || productDTO.getCategory() == null) {
			logger.warn("Validation failed: {}", productDTO);
			throw new LenskartApplicationException("Incomplete or invalid product data provided");
		}

		var category = categoryRepository.findByCategoryName(productDTO.getCategory());
		if (category == null) {
			logger.error("Category not found: {}", productDTO.getCategory());
			throw new LenskartApplicationException("Category '" + productDTO.getCategory() + "' not found");
		}

		Product product = new Product();
		product.setProductName(productDTO.getProductName());
		product.setProductPrice(productDTO.getProductPrice());
		product.setBrand(productDTO.getBrand());
		product.setCategory(category);
		product.setProductImage(productDTO.getProductImage());
		product.setQuantity(productDTO.getQuantity());
		productRepository.save(product);
		return "Product added successfully with ID: " + product.getProductId();

	}

	@Override
	@Transactional
	public String updateProduct(ProductDTO productDTO) throws LenskartApplicationException {
		logger.debug("Attempting to update product with ID: {}", productDTO.getProductId());
		Product product = productRepository.findById(productDTO.getProductId()).orElse(null);
		if (productDTO.getProductId() == 0) {
			logger.error("Invalid product details provided: {}", productDTO);
			throw new LenskartApplicationException("Invalid product details provided for update");
		}

		if (product == null) {
			logger.warn("Product not found for update with ID: {}", productDTO.getProductId());
			throw new LenskartApplicationException(
					"Cannot update. Product not found with ID: " + productDTO.getProductId());
		}

		product.setProductName(productDTO.getProductName());
		product.setProductPrice(productDTO.getProductPrice());
		product.setBrand(productDTO.getBrand());
		product.setQuantity(productDTO.getQuantity());
		if (productDTO.getProductImage() != null && !productDTO.getProductImage().isEmpty()) {
			product.setProductImage(productDTO.getProductImage());
		}

		if (productDTO.getCategory() != null && !productDTO.getCategory().isEmpty()) {
			var category = categoryRepository.findByCategoryName(productDTO.getCategory());
			if (category == null) {
				logger.error("Category not found: {}", productDTO.getCategory());
				throw new LenskartApplicationException("Category '" + productDTO.getCategory() + "' not found");
			}
			product.setCategory(category);
		}

		productRepository.save(product);
		logger.info("Product updated successfully for ID: {}", productDTO.getProductId());
		return "Product updated successfully for ID: " + productDTO.getProductId();

	}

	@Override
	@Transactional
	public String deleteProduct(int productId) {
		logger.debug("Attempting to delete product with ID: {}", productId);
		if (!productRepository.existsById(productId)) {
			logger.error("Delete failed. Product not found with ID: {}", productId);
			throw new LenskartApplicationException("Cannot delete. Product not found with ID: " + productId);
		}
		productRepository.deleteById(productId);
		logger.info("Product deleted successfully with ID: {}", productId);
		return "Product deleted successfully with ID: " + productId;

	}

	@Override
	public ProductDTO getById(int id) throws LenskartApplicationException {
		logger.debug("Fetching product details for ID: {}", id);
		Product product = productRepository.findById(id).orElse(null);
		if (product == null) {
			logger.error("Product not found with ID: {}", id);
			throw new LenskartApplicationException("Product not found with ID: " + id);

		}
		logger.info("Product retrieved successfully for ID: {}", id);
		return convertToDTO(product);
	}

	@Override
	public List<ProductDTO> getProductByBrand(String brandName) throws LenskartApplicationException {
		logger.debug("Fetching products for brand: {}", brandName);
		if (brandName == null || brandName.trim().isEmpty()) {
			throw new LenskartApplicationException("Brand name must not be empty");
		}
		List<Product> products = productRepository.findByBrand(brandName);
		if (products.isEmpty()) {
			throw new LenskartApplicationException("No products found for brand: " + brandName);
		}
		List<ProductDTO> dtos = new ArrayList<>();
		for (Product p : products)
			dtos.add(convertToDTO(p));
		logger.info("Found {} products under brand: {}", dtos.size(), brandName);
		return dtos;
	}

	@Override
	public List<ProductDTO> findAll() throws LenskartApplicationException {
		logger.debug("Attempting to retrieve all products");
		List<Product> products = productRepository.findAll();
		if (products.isEmpty()) {
			logger.warn("No products found in the database");
			throw new LenskartApplicationException("No products available in the database");
		}

		List<ProductDTO> dtos = new ArrayList<>();
		for (Product p : products)
			dtos.add(convertToDTO(p));
		logger.info("Successfully retrieved {} products", dtos.size());
		return dtos;
	}

	private ProductDTO convertToDTO(Product product) {
		ProductDTO dto = new ProductDTO();
		dto.setProductId(product.getProductId());
		dto.setProductName(product.getProductName());
		dto.setProductPrice(product.getProductPrice());
		dto.setProductImage(product.getProductImage());
		dto.setQuantity(product.getQuantity());
		dto.setBrand(product.getBrand());
		dto.setCategory(product.getCategory().getCategoryName());
		return dto;
	}
}

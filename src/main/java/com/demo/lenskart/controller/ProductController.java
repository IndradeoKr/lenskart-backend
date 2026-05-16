package com.demo.lenskart.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.demo.lenskart.dto.ProductDTO;
import com.demo.lenskart.service.IProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
@Tag(name = "Product Operations", description = "Endpoints for managing products, including creation, updates, deletion, and retrieval.")
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private IProductService productService;

	@Autowired
	public ProductController(IProductService productService) {

		this.productService = productService;
	}

	@Operation(summary = "Add a New Product", description = "Adds a new product to the catalog. Requires complete product details such as name, brand, price, and category.")
	@PostMapping
	public ResponseEntity<String> addProduct(@RequestBody @Valid ProductDTO productDTO) {
		logger.info("Request received to add product: {}", productDTO.getProductName());
		String productCreatedMessage = productService.addProduct(productDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(productCreatedMessage);
	}

	@Operation(summary = "Update an Existing Product", description = "Updates details of an existing product by its ID. Can modify fields like name, brand, price, and quantity.")
	@PutMapping
	public ResponseEntity<String> updateProduct(@RequestBody @Valid ProductDTO productDTO) {
		logger.info("Received update request for product ID: {}", productDTO.getProductId());
		String productupdateMessage = productService.updateProduct(productDTO);
		return ResponseEntity.status(HttpStatus.OK).body(productupdateMessage);

	}

	@Operation(summary = "Delete a Product", description = "Deletes a product from the catalog using its ID. Returns confirmation upon success.")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id) {
		logger.info("Received delete request for product ID: {}", id);
		String productdeleteMessage = productService.deleteProduct(id);
		return ResponseEntity.status(HttpStatus.OK).body(productdeleteMessage);

	}

	@Operation(summary = "Retrieve a Product by ID", description = "Fetches product details using the product ID. Returns full product information.")
	@GetMapping("/id/{id}")
	public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
		logger.debug("Fetching product by ID: {}", id);
		ProductDTO product = productService.getById(id);
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@Operation(summary = "Retrieve Products by Brand", description = "Fetches a list of all products under a specified brand name.")
	@GetMapping("/brand/{brand}")
	public ResponseEntity<List<ProductDTO>> getProductByBrand(@PathVariable String brand) {
		logger.debug("Fetching product by Brand: {}", brand);
		List<ProductDTO> products = productService.getProductByBrand(brand);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@Operation(summary = "Get All Products", description = "Returns a complete list of all products available in the catalog.")
	@GetMapping
	public ResponseEntity<List<ProductDTO>> getAllProducts() {
		logger.debug("Fetching Total products List");
		List<ProductDTO> products = productService.findAll();
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
}
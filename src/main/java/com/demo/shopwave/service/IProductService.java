package com.demo.shopwave.service;

import java.util.List;
import com.demo.shopwave.dto.ProductDTO;
import com.demo.shopwave.exception.ShopwaveApplicationException;

public interface IProductService {
	public ProductDTO getById(int id) throws ShopwaveApplicationException;

	public String updateProduct(ProductDTO productDTO) throws ShopwaveApplicationException;

	public String deleteProduct(int productId) throws ShopwaveApplicationException;

	public String addProduct(ProductDTO productDTO) throws ShopwaveApplicationException;

	public List<ProductDTO> findAll() throws ShopwaveApplicationException;

	public List<ProductDTO> getProductByBrand(String brandName) throws ShopwaveApplicationException;
}
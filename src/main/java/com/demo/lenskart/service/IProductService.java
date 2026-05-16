package com.demo.lenskart.service;

import java.util.List;
import com.demo.lenskart.dto.ProductDTO;
import com.demo.lenskart.exception.LenskartApplicationException;

public interface IProductService {
	public ProductDTO getById(int id) throws LenskartApplicationException;

	public String updateProduct(ProductDTO productDTO) throws LenskartApplicationException;

	public String deleteProduct(int productId) throws LenskartApplicationException;

	public String addProduct(ProductDTO productDTO) throws LenskartApplicationException;

	public List<ProductDTO> findAll() throws LenskartApplicationException;

	public List<ProductDTO> getProductByBrand(String brandName) throws LenskartApplicationException;
}
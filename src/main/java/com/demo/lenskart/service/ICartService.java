package com.demo.lenskart.service;

import com.demo.lenskart.dto.CartDTO;
import com.demo.lenskart.dto.CartViewDTO;
import com.demo.lenskart.exception.LenskartApplicationException;

public interface ICartService {
	public String addToCart(CartDTO cartDTO) throws LenskartApplicationException;

	public String increaseCartQuantity(CartDTO cartDTO) throws LenskartApplicationException;

	public String decreaseCartQuantity(CartDTO cartDTO) throws LenskartApplicationException;

	public String deleteCart(int productId) throws LenskartApplicationException;

	public CartViewDTO getCartForCustomer(int customerId) throws LenskartApplicationException;
	
	
	
}

package com.demo.shopwave.service;

import com.demo.shopwave.dto.CartDTO;
import com.demo.shopwave.dto.CartViewDTO;
import com.demo.shopwave.exception.ShopwaveApplicationException;

public interface ICartService {
	public String addToCart(CartDTO cartDTO) throws ShopwaveApplicationException;

	public String increaseCartQuantity(CartDTO cartDTO) throws ShopwaveApplicationException;

	public String decreaseCartQuantity(CartDTO cartDTO) throws ShopwaveApplicationException;

	public String deleteCart(int productId) throws ShopwaveApplicationException;

	public CartViewDTO getCartForCustomer(int customerId) throws ShopwaveApplicationException;
	
	
	
}

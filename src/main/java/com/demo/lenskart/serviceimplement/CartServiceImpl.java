/*
 * package com.demp.lenskart.serviceimplement;
 * 
 * import java.util.ArrayList; import java.util.Arrays; import
 * java.util.Optional;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Service;
 * 
 * import com.demp.lenskart.dto.CartDTO; import com.demp.lenskart.entity.Cart;
 * import com.demp.lenskart.entity.Product; import
 * com.demp.lenskart.entity.User; import
 * com.demp.lenskart.exception.LenskartApplicationException; import
 * com.demp.lenskart.repository.CartRepository; import
 * com.demp.lenskart.repository.ProductRepository; import
 * com.demp.lenskart.repository.UserRepository; import
 * com.demp.lenskart.service.ICartService;
 * 
 * @Service public class CartServiceImpl implements ICartService {
 * 
 * @Autowired private CartRepository cartRepository;
 * 
 * @Autowired private ProductRepository productRepository;
 * 
 * @Autowired private UserRepository userRepository;
 * 
 * @Override public String addToCart(CartDTO cartDTO) throws
 * LenskartApplicationException { Cart cart = new Cart(); Optional<User>
 * customerExisting = userRepository.findById(cartDTO.getCustomerId()); if
 * (!customerExisting.isPresent()) { throw new
 * LenskartApplicationException("customer not found"); } if
 * (!customerExisting.get().getRole().equalsIgnoreCase("CUSTOMER")) { throw new
 * LenskartApplicationException("Only customers are allowed to add product into the cart."
 * ); }
 * 
 * 
 * Optional<Product> productOpt =
 * productRepository.findById(cartDTO.getProductId());
 * 
 * if (!productOpt.isPresent()) { throw new
 * LenskartApplicationException("Product not found"); } Product product =
 * productOpt.get(); if(product.getQuantity()<cartDTO.getQuantity()) { throw new
 * LenskartApplicationException("product is out of stock"); }
 * cart.setCustomer(userRepository.findById(cartDTO.getCustomerId()).orElse(null
 * ));
 * 
 * 
 * cart.setProduct(new ArrayList<>(Arrays.asList(product))); double totalPrice =
 * product.getProductPrice() * cartDTO.getQuantity();
 * 
 * cart.setTotalQuantity(cartDTO.getQuantity()); cart.setTotalPrice(totalPrice);
 * 
 * Cart saved = cartRepository.save(cart);
 * customerExisting.get().setCarts(saved);
 * 
 * cartDTO.setId(saved.getId()); cartDTO.setQuantity(cart.getTotalQuantity());
 * cartDTO.setTotalPrice(cart.getTotalPrice());
 * cartDTO.setTotalPrice(totalPrice); return "cart added successfully"; }
 * 
 * @Override public String updateCart(CartDTO cartDTO) throws
 * LenskartApplicationException { Optional<Cart> cartOpt =
 * cartRepository.findById(cartDTO.getId()); if (!cartOpt.isPresent()) {
 * 
 * throw new LenskartApplicationException("Cart not found"); } Cart existingCart
 * = cartOpt.get();
 * 
 * if (existingCart.getProduct() == null) { throw new
 * RuntimeException("Cart has no products to update"); }
 * 
 * Product product = existingCart.getProduct().get(0);
 * existingCart.setTotalQuantity(existingCart.getTotalQuantity() +
 * cartDTO.getQuantity());
 * existingCart.setTotalPrice(existingCart.getTotalQuantity() *
 * product.getProductPrice());
 * 
 * cartRepository.save(existingCart);
 * cartDTO.setQuantity(existingCart.getTotalQuantity());
 * cartDTO.setTotalPrice(existingCart.getTotalPrice());
 * 
 * return "cart updated successfully"; }
 * 
 * @Override public String deleteCart(int cartId) throws
 * LenskartApplicationException { Cart cart =
 * cartRepository.findById(cartId).orElse(null); if (cart == null) { throw new
 * LenskartApplicationException("Cart with ID " + cartId + " does not exist.");
 * } if (cart.getCustomer() != null) throw new
 * LenskartApplicationException("user is already there so cart can not be deleted"
 * ); else {
 * 
 * //cart.getCustomer().setCarts(null); cart.setCustomer(null);
 * 
 * cart.setProduct(null); cartRepository.delete(cart); }
 * 
 * return "Cart deleted successfully";
 * 
 * } }
 */

package com.demo.lenskart.serviceimplement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.lenskart.dto.CartDTO;
import com.demo.lenskart.dto.CartViewDTO;
import com.demo.lenskart.entity.Cart;
import com.demo.lenskart.entity.Product;
import com.demo.lenskart.entity.User;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.CartRepository;
import com.demo.lenskart.repository.ProductRepository;
import com.demo.lenskart.repository.UserRepository;
import com.demo.lenskart.service.ICartService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements ICartService {

	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	String cartStatus = "Cart not found with ID: {}";
	private CartRepository cartRepository;

	private ProductRepository productRepository;

	private UserRepository userRepository;

	@Autowired
	public CartServiceImpl(ProductRepository productRepository, CartRepository cartRepository,
			UserRepository userRepository) {
		this.userRepository = userRepository;
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;

	}

	@Override
	@Transactional
	public String addToCart(CartDTO cartDTO) throws LenskartApplicationException {
		logger.info("Attempting to add cart for Customer ID: {}", cartDTO.getCustomerId());

		Optional<User> customerExisting = userRepository.findById(cartDTO.getCustomerId());
		if (!customerExisting.isPresent()) {
			logger.error("Customer not found with ID: {}", cartDTO.getCustomerId());
			throw new LenskartApplicationException("customer not found");
		}

		if (!customerExisting.get().getRole().equalsIgnoreCase("CUSTOMER")) {
			logger.warn("User with ID {} is not a CUSTOMER", cartDTO.getCustomerId());
			throw new LenskartApplicationException("Only customers are allowed to add product into the cart.");
		}

		Optional<Product> productOpt = productRepository.findById(cartDTO.getProductId());
		if (!productOpt.isPresent()) {
			logger.error("Product not found with ID: {}", cartDTO.getProductId());
			throw new LenskartApplicationException("Product not found");
		}

		Product product = productOpt.get();
		if (product.getQuantity() < cartDTO.getQuantity()) {
			logger.warn("Requested quantity {} exceeds available stock for Product ID: {}", cartDTO.getQuantity(),
					product.getProductId());
			throw new LenskartApplicationException("product is out of stock");
		}

		Cart cart = cartRepository.findByCustomerUserId(cartDTO.getCustomerId());
		if (cart == null) {
			cart = new Cart();
			cart.setCustomer(customerExisting.get());
		}
		cart.setProduct(new ArrayList<>(Arrays.asList(product)));

		double totalPrice = product.getProductPrice() * cartDTO.getQuantity();
		cart.setTotalQuantity(cartDTO.getQuantity());
		cart.setTotalPrice(totalPrice);

		Cart saved = cartRepository.save(cart);
		// The cart entity is the owning side, saving it handles the foreign key.

		cartDTO.setId(saved.getId());
		cartDTO.setQuantity(cart.getTotalQuantity());
		logger.info("Cart saved successfully with ID: {}", saved.getId());
		return "cart added successfully";
	}

	@Override
	public CartViewDTO getCartForCustomer(int customerId) throws LenskartApplicationException {
		Cart cart = cartRepository.findByCustomerUserId(customerId);
		if (cart == null) {
			throw new LenskartApplicationException("Cart not found for customer ID: " + customerId);
		}
		if (cart.getProduct() == null || cart.getProduct().isEmpty()) {
			throw new LenskartApplicationException("Cart has no products");
		}

		Product product = cart.getProduct().get(0);
		CartViewDTO dto = new CartViewDTO();
		dto.setCartId(cart.getId());
		dto.setCustomerId(customerId);
		dto.setProductId(product.getProductId());
		dto.setProductName(product.getProductName());
		dto.setProductPrice(product.getProductPrice());
		dto.setProductImage(product.getProductImage());
		dto.setBrand(product.getBrand());
		dto.setCategoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null);
		dto.setTotalQuantity(cart.getTotalQuantity());
		dto.setTotalPrice(cart.getTotalPrice());
		return dto;
	}

	@Override
	@Transactional
	public String increaseCartQuantity(CartDTO cartDTO) throws LenskartApplicationException {
		logger.info("Initiating update for Cart ID: {}", cartDTO.getId());

		Optional<Cart> cartOpt = cartRepository.findById(cartDTO.getId());
		if (!cartOpt.isPresent()) {
			logger.error(cartStatus, cartDTO.getId());
			throw new LenskartApplicationException("Cart not found");
		}

		Cart existingCart = cartOpt.get();
		if (existingCart.getProduct() == null) {
			logger.error("Cart ID {} has no product associated", existingCart.getId());
			throw new LenskartApplicationException("Cart has no products to update");
		}

		Product product = existingCart.getProduct().get(0);
		int updatedQuantity = existingCart.getTotalQuantity() + cartDTO.getQuantity();
		if (updatedQuantity > product.getQuantity()) {
			throw new LenskartApplicationException("product not available for this quantity");
		}
		double updatedPrice = updatedQuantity * product.getProductPrice();

		existingCart.setTotalQuantity(updatedQuantity);
		existingCart.setTotalPrice(updatedPrice);

		cartRepository.save(existingCart);
		cartDTO.setQuantity(updatedQuantity);

		logger.info("Cart updated: Quantity = {}", updatedQuantity);

		return "cart updated successfully";
	}

	@Override
	@Transactional
	public String decreaseCartQuantity(CartDTO cartDTO) throws LenskartApplicationException {
		logger.info("Initiating update for Cart ID: {}", cartDTO.getId());

		Optional<Cart> cartOpt = cartRepository.findById(cartDTO.getId());
		if (!cartOpt.isPresent()) {
			logger.error(cartStatus, cartDTO.getId());
			throw new LenskartApplicationException("Cart not found");
		}

		Cart existingCart = cartOpt.get();
		if (existingCart.getProduct() == null) {
			logger.error("Cart ID {} has no product associated", existingCart.getId());
			throw new LenskartApplicationException("Cart has no products to update");
		}

		Product product = existingCart.getProduct().get(0);
		int updatedQuantity = existingCart.getTotalQuantity() - cartDTO.getQuantity();
		if (updatedQuantity < 0) {
			throw new LenskartApplicationException("cart is empty, you can't decrease more product");
		}
		double updatedPrice = updatedQuantity * product.getProductPrice();

		existingCart.setTotalQuantity(updatedQuantity);
		existingCart.setTotalPrice(updatedPrice);

		cartRepository.save(existingCart);
		cartDTO.setQuantity(updatedQuantity);

		logger.info("Cart updated: Quantity = {}", updatedQuantity);

		return "cart updated successfully";
	}

	@Override
	@Transactional
	public String deleteCart(int cartId) throws LenskartApplicationException {
		logger.info("Request received to delete Cart ID: {}", cartId);

		Cart cart = cartRepository.findById(cartId).orElse(null);
		if (cart == null) {
			logger.error(cartStatus, cartId);
			throw new LenskartApplicationException("Cart with ID " + cartId + " does not exist.");
		}

		if (cart.getCustomer() != null) {
			logger.warn("Cart ID {} has customer linked, cannot delete", cartId);
			throw new LenskartApplicationException("user is already there so cart can not be deleted");
		}

		cart.setCustomer(null);
		cart.setProduct(null);
		cartRepository.delete(cart);

		logger.info("Cart ID {} deleted successfully", cartId);

		return "Cart deleted successfully";
	}
}

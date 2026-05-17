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
 * com.demp.lenskart.exception.ShopwaveApplicationException; import
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
 * ShopwaveApplicationException { Cart cart = new Cart(); Optional<User>
 * customerExisting = userRepository.findById(cartDTO.getCustomerId()); if
 * (!customerExisting.isPresent()) { throw new
 * ShopwaveApplicationException("customer not found"); } if
 * (!customerExisting.get().getRole().equalsIgnoreCase("CUSTOMER")) { throw new
 * ShopwaveApplicationException("Only customers are allowed to add product into the cart."
 * ); }
 * 
 * 
 * Optional<Product> productOpt =
 * productRepository.findById(cartDTO.getProductId());
 * 
 * if (!productOpt.isPresent()) { throw new
 * ShopwaveApplicationException("Product not found"); } Product product =
 * productOpt.get(); if(product.getQuantity()<cartDTO.getQuantity()) { throw new
 * ShopwaveApplicationException("product is out of stock"); }
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
 * ShopwaveApplicationException { Optional<Cart> cartOpt =
 * cartRepository.findById(cartDTO.getId()); if (!cartOpt.isPresent()) {
 * 
 * throw new ShopwaveApplicationException("Cart not found"); } Cart existingCart
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
 * ShopwaveApplicationException { Cart cart =
 * cartRepository.findById(cartId).orElse(null); if (cart == null) { throw new
 * ShopwaveApplicationException("Cart with ID " + cartId + " does not exist.");
 * } if (cart.getCustomer() != null) throw new
 * ShopwaveApplicationException("user is already there so cart can not be deleted"
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

package com.demo.shopwave.serviceimplement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.shopwave.dto.CartDTO;
import com.demo.shopwave.dto.CartViewDTO;
import com.demo.shopwave.entity.Cart;
import com.demo.shopwave.entity.Product;
import com.demo.shopwave.entity.User;
import com.demo.shopwave.exception.ShopwaveApplicationException;
import com.demo.shopwave.repository.CartRepository;
import com.demo.shopwave.repository.ProductRepository;
import com.demo.shopwave.repository.UserRepository;
import com.demo.shopwave.service.ICartService;

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
	public String addToCart(CartDTO cartDTO) throws ShopwaveApplicationException {
		logger.info("Attempting to add cart for Customer ID: {}", cartDTO.getCustomerId());

		Optional<User> customerExisting = userRepository.findById(cartDTO.getCustomerId());
		if (!customerExisting.isPresent()) {
			logger.error("Customer not found with ID: {}", cartDTO.getCustomerId());
			throw new ShopwaveApplicationException("customer not found");
		}

		if (!customerExisting.get().getRole().equalsIgnoreCase("CUSTOMER")) {
			logger.warn("User with ID {} is not a CUSTOMER", cartDTO.getCustomerId());
			throw new ShopwaveApplicationException("Only customers are allowed to add product into the cart.");
		}

		Optional<Product> productOpt = productRepository.findById(cartDTO.getProductId());
		if (!productOpt.isPresent()) {
			logger.error("Product not found with ID: {}", cartDTO.getProductId());
			throw new ShopwaveApplicationException("Product not found");
		}

		Product product = productOpt.get();
		if (product.getQuantity() < cartDTO.getQuantity()) {
			logger.warn("Requested quantity {} exceeds available stock for Product ID: {}", cartDTO.getQuantity(),
					product.getProductId());
			throw new ShopwaveApplicationException("product is out of stock");
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
	public CartViewDTO getCartForCustomer(int customerId) throws ShopwaveApplicationException {
		Cart cart = cartRepository.findByCustomerUserId(customerId);
		if (cart == null) {
			throw new ShopwaveApplicationException("Cart not found for customer ID: " + customerId);
		}
		if (cart.getProduct() == null || cart.getProduct().isEmpty()) {
			throw new ShopwaveApplicationException("Cart has no products");
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
	public String increaseCartQuantity(CartDTO cartDTO) throws ShopwaveApplicationException {
		logger.info("Initiating update for Cart ID: {}", cartDTO.getId());

		Optional<Cart> cartOpt = cartRepository.findById(cartDTO.getId());
		if (!cartOpt.isPresent()) {
			logger.error(cartStatus, cartDTO.getId());
			throw new ShopwaveApplicationException("Cart not found");
		}

		Cart existingCart = cartOpt.get();
		if (existingCart.getProduct() == null) {
			logger.error("Cart ID {} has no product associated", existingCart.getId());
			throw new ShopwaveApplicationException("Cart has no products to update");
		}

		Product product = existingCart.getProduct().get(0);
		int updatedQuantity = existingCart.getTotalQuantity() + cartDTO.getQuantity();
		if (updatedQuantity > product.getQuantity()) {
			throw new ShopwaveApplicationException("product not available for this quantity");
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
	public String decreaseCartQuantity(CartDTO cartDTO) throws ShopwaveApplicationException {
		logger.info("Initiating update for Cart ID: {}", cartDTO.getId());

		Optional<Cart> cartOpt = cartRepository.findById(cartDTO.getId());
		if (!cartOpt.isPresent()) {
			logger.error(cartStatus, cartDTO.getId());
			throw new ShopwaveApplicationException("Cart not found");
		}

		Cart existingCart = cartOpt.get();
		if (existingCart.getProduct() == null) {
			logger.error("Cart ID {} has no product associated", existingCart.getId());
			throw new ShopwaveApplicationException("Cart has no products to update");
		}

		Product product = existingCart.getProduct().get(0);
		int updatedQuantity = existingCart.getTotalQuantity() - cartDTO.getQuantity();
		if (updatedQuantity < 0) {
			throw new ShopwaveApplicationException("cart is empty, you can't decrease more product");
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
	public String deleteCart(int cartId) throws ShopwaveApplicationException {
		logger.info("Request received to delete Cart ID: {}", cartId);

		Cart cart = cartRepository.findById(cartId).orElse(null);
		if (cart == null) {
			logger.error(cartStatus, cartId);
			throw new ShopwaveApplicationException("Cart with ID " + cartId + " does not exist.");
		}

		if (cart.getCustomer() != null) {
			logger.warn("Cart ID {} has customer linked, cannot delete", cartId);
			throw new ShopwaveApplicationException("user is already there so cart can not be deleted");
		}

		cart.setCustomer(null);
		cart.setProduct(null);
		cartRepository.delete(cart);

		logger.info("Cart ID {} deleted successfully", cartId);

		return "Cart deleted successfully";
	}
}


package com.demo.lenskart.serviceimplement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.lenskart.dto.CartDTO;
import com.demo.lenskart.entity.Cart;
import com.demo.lenskart.entity.Product;
import com.demo.lenskart.entity.User;
import com.demo.lenskart.exception.LenskartApplicationException;
import com.demo.lenskart.repository.CartRepository;
import com.demo.lenskart.repository.ProductRepository;
import com.demo.lenskart.repository.UserRepository;

class CartServiceImplTest {

	@Mock
	private CartRepository cartRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private CartServiceImpl cartService;

	private User customer;
	private Product product;
	private CartDTO cartDTO;
	private Cart cart;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		customer = new User();
		customer.setUserid(1);
		customer.setRole("CUSTOMER");

		product = new Product();
		product.setProductId(1);
		product.setProductPrice(100.0);
		product.setQuantity(10);

		cartDTO = new CartDTO();
		cartDTO.setCustomerId(1);
		cartDTO.setProductId(1);
		cartDTO.setQuantity(2);

		cart = new Cart();
		cart.setId(1);
		cart.setCustomer(customer);
		cart.setProduct(Arrays.asList(product));
		cart.setTotalQuantity(2);
		cart.setTotalPrice(200.0);
	}

	/* test cases for adding a cart */

	@Test
    void testAddToCart_Positive() {
        when(userRepository.findById(1)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        String result = cartService.addToCart(cartDTO);
        assertEquals("cart added successfully", result);
    }

	@Test
    void testAddToCart_Negative_CustomerNotFound(){
        when(userRepository.findById(1)).thenReturn(Optional.empty()); /*here we simulate that customer doesn't exist*/ 
        LenskartApplicationException ex = assertThrows(LenskartApplicationException.class, () -> {
            cartService.addToCart(cartDTO);
        });
        /*we expect this method to throw an exception and catch it here*/
        
        assertEquals("customer not found", ex.getMessage());
    }
	

	/* test cases for Increase quantity of product in the cart */

	@Test
	void testIncreaseCartQuantity_Positive() throws Exception {
		cart.setId(1);
		cart.setProduct(Arrays.asList(product)); // Ensure product is in cart
		cart.setTotalQuantity(1);
		cart.setTotalPrice(100);

		cartDTO.setId(1);
		cartDTO.setQuantity(4); // Trying to add 4 more units

		when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);

		String result = cartService.increaseCartQuantity(cartDTO);
		assertEquals("cart updated successfully", result);
	}

	@Test
	void testIncreaseCartQuantity_Negative() {
		cart.setTotalQuantity(1);
		cart.setTotalPrice(100);
		cartDTO.setQuantity(20); // Too much quantity greater than product

		when(cartRepository.findById(1)).thenReturn(Optional.of(cart));

		assertThrows(LenskartApplicationException.class, () -> cartService.increaseCartQuantity(cartDTO));
	}
	/* test cases for decrease quantity of product in the cart */

	@Test
	void testDecreaseCartQuantity_Positive() throws Exception {
		cart.setId(1);
		cart.setTotalQuantity(3);
		cart.setTotalPrice(300);
		cart.setProduct(Arrays.asList(product)); // Make sure product exists
		cartDTO.setId(1);
		cartDTO.setQuantity(1); // Decrease by 1

		when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);

		String result = cartService.decreaseCartQuantity(cartDTO);
		assertEquals("cart updated successfully", result);
	}

	@Test
	void testDecreaseCartQuantity_Negative() {
		cart.setTotalQuantity(1);
		cart.setTotalPrice(100);
		cartDTO.setQuantity(2); // Decrease more than quantity

		when(cartRepository.findById(1)).thenReturn(Optional.of(cart));

		assertThrows(LenskartApplicationException.class, () -> cartService.decreaseCartQuantity(cartDTO));
	}

	/* test cases for deleting the cart */

	@Test
	void testDeleteCart_Positive() {
		cart.setCustomer(null); // No linked user

		when(cartRepository.findById(1)).thenReturn(Optional.of(cart));
		doNothing().when(cartRepository).delete(cart);

		String result = cartService.deleteCart(1);
		assertEquals("Cart deleted successfully", result);
	}

	@Test
	void testDeleteCart_Negative_CustomerLinked() {
		cart.setCustomer(customer); // Linked user

		when(cartRepository.findById(1)).thenReturn(Optional.of(cart));

		Exception ex = assertThrows(LenskartApplicationException.class, () -> cartService.deleteCart(1));

		assertEquals("user is already there so cart can not be deleted", ex.getMessage());
	}
}

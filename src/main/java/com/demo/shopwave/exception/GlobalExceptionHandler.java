package com.demo.shopwave.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ShopwaveApplicationException.class)
	public ResponseEntity<String> handleEmployeeNotFoundException(ShopwaveApplicationException exc) {
		logger.error("Exception while processing product: {}", exc.getMessage(), exc);
		return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidation(MethodArgumentNotValidException exc) {
		String errorMessage = exc.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		logger.warn("Validation failed: {}", errorMessage);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException exc) {
		logger.error("Constraint violation encountered: {}", exc.getMessage(), exc);
		return new ResponseEntity<>(exc.getMessage(), HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException exc) {
		logger.error("Constraint violation encountered: {}", exc.getMessage(), exc);
		return new ResponseEntity<>(exc.getMessage(), HttpStatus.CONFLICT);
	}

}

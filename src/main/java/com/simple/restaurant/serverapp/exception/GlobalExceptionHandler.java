package com.simple.restaurant.serverapp.exception;

import com.simple.restaurant.serverapp.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataBaseAccessException.class)
	public ResponseEntity<?> databaseAccessHandling(DataBaseAccessException exception, WebRequest request){
		ErrorDto errorDetails = new ErrorDto(
				exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundHandling(ResourceNotFoundException exception, WebRequest request){
		ErrorDto errorDetails = new ErrorDto(
				exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException exception, WebRequest request) {
		ErrorDto errorDetails = new ErrorDto(
				exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> handleWrongParams(MethodArgumentTypeMismatchException exception, WebRequest request) {
		ErrorDto errorDetails = new ErrorDto(
				exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoItemsException.class)
	public ResponseEntity<?> handleNoItems(NoItemsException exception, WebRequest request){
		ErrorDto errorDetails = new ErrorDto(
				exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(OrderCreationException.class)
	public ResponseEntity<?> handleOrderCreationError(OrderCreationException exception, WebRequest request){
		ErrorDto errorDetails = new ErrorDto(
				exception.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	}
}

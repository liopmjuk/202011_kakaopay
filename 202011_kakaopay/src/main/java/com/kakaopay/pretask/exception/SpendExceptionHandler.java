package com.kakaopay.pretask.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * Application에서 발생하는 모든 Exception를 처리해주는 Handler
 * @author Jeon
 *
 */
@ControllerAdvice
public class SpendExceptionHandler {
	Logger logger = LoggerFactory.getLogger(SpendExceptionHandler.class);
	
	@ExceptionHandler(value=SpendException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(SpendException ex) {
		final SpendErrorCode error = ex.getError();
		logger.error("errorCode: {}, errorMsg: {}", error.getErrorCode(), error.getErrorMsg());
		
		ErrorResponse response = ErrorResponse.of(error);
		
		return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value=NumberFormatException.class)
	public ResponseEntity<ErrorResponse> handleNumberFormatException(NumberFormatException ex) {
//		final SpendErrorCode error = ex.getError();
//		logger.error("errorCode: {}, errorMsg: {}", error.getErrorCode(), error.getErrorMsg());
//		
//		ErrorResponse response = ErrorResponse.of(error);
		
		return new ResponseEntity("", HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value=HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
//		final SpendErrorCode error = ex.getError();
//		logger.error("errorCode: {}, errorMsg: {}", error.getErrorCode(), error.getErrorMsg());
//		
//		ErrorResponse response = ErrorResponse.of(error);
		
		return new ResponseEntity("", HttpStatus.BAD_REQUEST);
	}
}

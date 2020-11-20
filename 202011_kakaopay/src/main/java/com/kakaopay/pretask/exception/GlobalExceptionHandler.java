package com.kakaopay.pretask.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value=SpendException.class)
	public ResponseEntity<?> handleCustomException(SpendException ex) {
		final SpendErrorCode error = ex.getError();
		logger.error("errorCode: {}, errorMsg: {}", error.getErrorCode(), error.getErrorMsg());
		
//		return new ResponseEntity(new FailureOutput(ex), HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity("", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

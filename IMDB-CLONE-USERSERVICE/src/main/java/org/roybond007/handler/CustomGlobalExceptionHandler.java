package org.roybond007.handler;

import org.roybond007.exception.CustomValidationException;
import org.roybond007.exception.UserCreateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {

	
	@ExceptionHandler(value = {CustomValidationException.class})
	public ResponseEntity<?> showValidationErrorMsg(CustomValidationException exception){
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getErrorResponseBody());
	}
	
	@ExceptionHandler(value = {UserCreateException.class})
	public ResponseEntity<?> showUserCreationErrorMsg(UserCreateException exception){
		
		return ResponseEntity.internalServerError().body(exception.getErrorResponseBody());
	}
	
}

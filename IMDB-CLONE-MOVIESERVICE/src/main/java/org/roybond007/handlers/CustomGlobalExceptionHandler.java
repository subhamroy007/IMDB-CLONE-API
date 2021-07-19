package org.roybond007.handlers;

import org.roybond007.exceptions.CustomValidationException;
import org.roybond007.exceptions.MovieEntityNotFoundException;
import org.roybond007.exceptions.MovieUploadFailedException;
import org.roybond007.model.dto.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;
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

	@ExceptionHandler(value = {MovieUploadFailedException.class})
	public ResponseEntity<?> showMovieUploadErrorMsg(MovieUploadFailedException exception){
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getErrorResponseBody());
	}
	
	@ExceptionHandler(value = {MovieEntityNotFoundException.class})
	public ResponseEntity<?> showMovieNotFoundMsg(MovieEntityNotFoundException exception){
		
		ErrorResponseBody errorResponseBody = exception.getErrorResponseBody();
		
		if(errorResponseBody.getCode() == ErrorUtility.ENTITY_NOT_FOUND_CODE)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseBody);
		
		return ResponseEntity.internalServerError().body(errorResponseBody);
		
	}
	
}

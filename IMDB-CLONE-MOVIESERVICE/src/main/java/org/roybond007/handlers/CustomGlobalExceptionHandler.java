package org.roybond007.handlers;



import org.roybond007.exceptions.CustomException;
import org.roybond007.utils.ErrorUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomGlobalExceptionHandler {
	
	@ExceptionHandler(value = {CustomException.class})
	public ResponseEntity<?> showErrorMsg(CustomException exception){
		
		if(exception.getErrorCode() == ErrorUtility.DATA_LAYER_ERROR_CODE) {
			return ResponseEntity.internalServerError().body(exception.getErrorResponseBody());
		}else {
			return ResponseEntity.badRequest().body(exception.getErrorResponseBody());
		}
		
	}
	
	
}

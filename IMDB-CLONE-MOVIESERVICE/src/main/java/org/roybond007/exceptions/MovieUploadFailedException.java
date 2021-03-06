package org.roybond007.exceptions;

import java.util.HashMap;
import java.util.List;

import org.roybond007.model.dto.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;
import org.springframework.validation.FieldError;

public class MovieUploadFailedException extends CustomException {

	private static final long serialVersionUID = -1798424549687742247L;
	
	public MovieUploadFailedException(int errorCode, String errorMsg, Object reason) {
		super(errorCode, errorMsg, reason);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ErrorResponseBody getErrorResponseBody() {
		
		HashMap<String, Object> map = new HashMap<>();
		
		if(errorCode == ErrorUtility.VALIDATION_FAILED_CODE) {
			
			List<FieldError> fieldErrors = (List<FieldError>)reason;
			
			for (FieldError fieldError : fieldErrors) {
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
		}else if(errorCode == ErrorUtility.DATA_LAYER_ERROR_CODE || errorCode == ErrorUtility.FILE_SYSTEM_ERROR_CODE) {
			//body not needed
		}
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(errorCode, errorMsg, map);
		
		return errorResponseBody;
	}
}

package org.roybond007.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.roybond007.model.helper.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;
import org.springframework.validation.FieldError;

public class SignUpFailedException extends CustomException {

	private static final long serialVersionUID = 4529946766570846339L;

	public SignUpFailedException(int errorCode, String errorMsg, Object reason) {
		super(errorCode, errorMsg, reason);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public ErrorResponseBody getErrorResponseBody() {
		
		Map<String, Object> map = new HashMap<>();
		
		if(errorCode == ErrorUtility.VALIDATION_FAILED_CODE) {
			
			List<FieldError> fieldErrors = (List<FieldError>)reason;
			
			for (FieldError fieldError : fieldErrors) {
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			
		}else if(errorCode == ErrorUtility.DATA_LAYER_ERROR) {
			//no body needed
		}
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(errorCode, errorMsg, map);
		
		return errorResponseBody;
	}

}

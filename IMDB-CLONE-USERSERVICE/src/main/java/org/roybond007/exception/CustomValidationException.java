package org.roybond007.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.roybond007.model.helper.ErrorResponseBody;
import org.springframework.validation.FieldError;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomValidationException extends RuntimeException {

	private static final long serialVersionUID = -2935925108727618625L;

	private final List<FieldError> FIELD_ERRORS;
	
	private final int ERROR_CODE;

	private final String ERROR_MSG;

	public CustomValidationException(List<FieldError> fieldErrors, int code, String msg) {
		super("VALIDATION FAILED");
		this.FIELD_ERRORS = fieldErrors;
		this.ERROR_CODE = code;
		this.ERROR_MSG = msg;
	}
	
	public ErrorResponseBody getErrorResponseBody() {
		
		Map<String, Object> map = new HashMap<>();
		
		for (FieldError fieldError : FIELD_ERRORS) {
			map.put(fieldError.getField(), fieldError.getDefaultMessage());
		}
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(ERROR_CODE, ERROR_MSG, map);
		
		return errorResponseBody;
	}
	
}

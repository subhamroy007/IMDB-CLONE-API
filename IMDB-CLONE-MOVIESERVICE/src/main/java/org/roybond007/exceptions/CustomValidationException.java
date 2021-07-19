package org.roybond007.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.roybond007.model.dto.ErrorResponseBody;
import org.springframework.validation.FieldError;

public class CustomValidationException extends RuntimeException {

	private static final long serialVersionUID = 8581481390846277196L;

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

package org.roybond007.exception;

import java.util.HashMap;

import org.roybond007.model.helper.ErrorResponseBody;

public class UserEntityUpdateFailedException extends RuntimeException {

	private static final long serialVersionUID = 1956449995705013987L;

	private final int ERROR_CODE;

	private final String ERROR_MSG;
	
	public UserEntityUpdateFailedException(int code, String msg) {
		super("DATABASE SAVE FAILED");
		this.ERROR_CODE = code;
		this.ERROR_MSG = msg;
	}
	
	public ErrorResponseBody getErrorResponseBody() {
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(ERROR_CODE, ERROR_MSG, new HashMap<>());
	
		return errorResponseBody;
	}
	
}

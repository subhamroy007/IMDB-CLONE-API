package org.roybond007.exceptions;

import java.util.HashMap;

import org.roybond007.model.dto.ErrorResponseBody;

public class MovieUploadFailedException extends RuntimeException {

	private static final long serialVersionUID = -1798424549687742247L;
	
	private final int ERROR_CODE;

	private final String ERROR_MSG;
	
	public MovieUploadFailedException(int code, String msg) {
		super("DATABASE SAVE FAILED");
		this.ERROR_CODE = code;
		this.ERROR_MSG = msg;
	}
	
	public ErrorResponseBody getErrorResponseBody() {
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(ERROR_CODE, ERROR_MSG, new HashMap<>());
	
		return errorResponseBody;
	}
	
}
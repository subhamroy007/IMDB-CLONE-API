package org.roybond007.exception;

import java.util.HashMap;
import java.util.Map;

import org.roybond007.model.dto.ErrorResponseBody;

public class UserEntityNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -7110210570991996158L;

	private final int ERROR_CODE;

	private final String ERROR_MSG;
	
	private final String USER_ID;

	public UserEntityNotFoundException(int ERROR_CODE, String ERROR_MSG, String userId) {
		super("USER ENTITY NOT FOUND");
		this.ERROR_CODE = ERROR_CODE;
		this.ERROR_MSG = ERROR_MSG;
		this.USER_ID = userId;
	}
	
	public ErrorResponseBody getErrorResponseBody() {
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("userid", USER_ID);
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(ERROR_CODE, ERROR_MSG, map);
		
		return errorResponseBody;
	}
	
}

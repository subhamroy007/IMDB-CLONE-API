package org.roybond007.exception;

import java.util.HashMap;
import java.util.Map;

import org.roybond007.model.helper.ErrorResponseBody;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UserVerificationException extends RuntimeException {

	private static final long serialVersionUID = -3129945509406761401L;

	private final int ERROR_CODE;

	private final String ERROR_MSG;
	
	private final String USER_ID;

	public UserVerificationException(int ERROR_CODE, String ERROR_MSG, String userId) {
		super("USER LOGIN FAILED");
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

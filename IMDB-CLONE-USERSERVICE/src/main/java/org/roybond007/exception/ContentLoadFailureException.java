package org.roybond007.exception;

import java.util.HashMap;
import java.util.Map;

import org.roybond007.model.helper.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;

public class ContentLoadFailureException extends RuntimeException {

	private static final long serialVersionUID = 4645388361928644649L;

	private int errorCode;
	private String errorMsg;
	private Object reason;

	
	
	public ContentLoadFailureException(int errorCode, String errorMsg,@Nullable Object reason) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.reason = reason;
	}



	public ErrorResponseBody getErrorResponseBody() {

		Map<String, Object> map = new HashMap<>();

		ErrorResponseBody errorResponseBody = new ErrorResponseBody();
		errorResponseBody.setCode(errorCode);
		errorResponseBody.setMsg(errorMsg);
		if(errorCode == ErrorUtility.ENTITY_NOT_FOUND)
			map.put("userId", (String)reason);
		
		errorResponseBody.setReason(map);
		
		return errorResponseBody;
	}
}

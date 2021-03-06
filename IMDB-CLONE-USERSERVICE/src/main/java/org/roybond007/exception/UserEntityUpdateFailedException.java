package org.roybond007.exception;

import java.util.HashMap;
import java.util.Map;

import org.roybond007.model.helper.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;

public class UserEntityUpdateFailedException extends CustomException {

	private static final long serialVersionUID = 1956449995705013987L;


	public UserEntityUpdateFailedException(int errorCode, String errorMsg, Object reason) {
		super(errorCode, errorMsg, reason);

	}
	
	@Override
	public ErrorResponseBody getErrorResponseBody() {
		
		Map<String, Object> map = new HashMap<>();
		
		if(errorCode == ErrorUtility.ENTITY_NOT_FOUND) {
			map.put("userId", (String)reason);
		}else if(errorCode == ErrorUtility.DATA_LAYER_ERROR) {
			//no body needed
		}
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(errorCode, errorMsg, map);
		
		return errorResponseBody;
	}
	
	
	
	
}

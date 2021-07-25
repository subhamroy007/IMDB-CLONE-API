package org.roybond007.exception;

import java.util.HashMap;
import java.util.Map;

import org.roybond007.model.helper.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;

public class SignInFailedException extends CustomException {

	private static final long serialVersionUID = -6220825232968971972L;

	public SignInFailedException(int errorCode, String errorMsg, Object reason) {
		super(errorCode, errorMsg, reason);
		
	}

	@Override
	public ErrorResponseBody getErrorResponseBody() {
		
		Map<String, Object> map = new HashMap<>();
		
		if(errorCode == ErrorUtility.CREDENTIAL_NOT_MATCH_ERROR_CODE) {
			map.put("userId", (String)reason);
		}else if(errorCode == ErrorUtility.DATA_LAYER_ERROR) {
			//no body needed
		}
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(errorCode, errorMsg, map);
		
		return errorResponseBody;
	}

}

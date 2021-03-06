package org.roybond007.exception;

import java.util.HashMap;
import java.util.Map;

import org.roybond007.model.helper.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;
import org.springframework.lang.Nullable;

public class ContentLoadFailureException extends  CustomException{

	private static final long serialVersionUID = 4645388361928644649L;
	
	
	public ContentLoadFailureException(int errorCode, String errorMsg,@Nullable Object reason) {
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

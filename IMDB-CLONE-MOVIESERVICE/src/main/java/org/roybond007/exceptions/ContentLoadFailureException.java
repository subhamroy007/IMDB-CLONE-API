package org.roybond007.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.roybond007.model.dto.ErrorResponseBody;
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

		if(errorCode == ErrorUtility.ENTITY_NOT_FOUND_CODE) {
			map.put("id", (String)reason);
		}else if(errorCode == ErrorUtility.DATA_LAYER_ERROR_CODE) {
			//body not needed
		}
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(errorCode, errorMsg, map);
		
		return errorResponseBody;
	}



	
}

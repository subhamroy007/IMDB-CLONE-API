package org.roybond007.exceptions;

import java.util.HashMap;

import org.roybond007.model.dto.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;

public class EntityUpdateFailedException extends CustomException {
	
	private static final long serialVersionUID = -978298841891235149L;
	
	
	public EntityUpdateFailedException(int errorCode, String errorMsg, Object reason) {
		super(errorCode, errorMsg, reason);
		
	}
	
	
	@Override
	public ErrorResponseBody getErrorResponseBody() {
		
		HashMap<String, Object> map = new HashMap<>();
		
		if(errorCode == ErrorUtility.ENTITY_NOT_FOUND_CODE) {
			map.put("id", (String)reason);
		}
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(errorCode, errorMsg, map);
		return errorResponseBody;
	}

	
}

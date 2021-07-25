package org.roybond007.exceptions;

import java.util.HashMap;

import org.roybond007.model.dto.ErrorResponseBody;
import org.roybond007.utils.ErrorUtility;

public class ReviewUploadFailedException extends CustomException{

	private static final long serialVersionUID = -1798424549687752247L;
	
	   
    public ReviewUploadFailedException(int errorCode, String errorMsg, Object reason) {
		super(errorCode, errorMsg, reason);
		// TODO Auto-generated constructor stub
	}
    
    @Override
    public ErrorResponseBody getErrorResponseBody() {
    	
    	HashMap<String, Object> map = new HashMap<>();
    	
    	if(errorCode == ErrorUtility.DATA_LAYER_ERROR_CODE) {
    		//body not needed
    	}
    	
    	ErrorResponseBody errorResponseBody = new ErrorResponseBody(errorCode, errorMsg, map);
    	
    	return errorResponseBody;
    }

}

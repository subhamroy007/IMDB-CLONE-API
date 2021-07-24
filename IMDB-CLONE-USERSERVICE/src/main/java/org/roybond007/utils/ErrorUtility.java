package org.roybond007.utils;

public class ErrorUtility {

	/*
	 ERROR CODES SENT IN THE ERROR RESPONSE FOR EASE OF DEVELOPMENT
	*/
	
	//returned to indicate there are some validation failed in some form-fields and a key-value map of reason is returned where 
	//key is the form-field and value is the corresponding error message
	public static final int VALIDATION_FAILED_CODE = 100;
	
	
	//returned to indicate some entity is not available in the database which is mandatory for request processing
	//ex: userId, movieId, replyId etc and a key value map of reason is returned where key is the entityId and value is the
	//required value
	public static final int ENTITY_NOT_FOUND = 102;
	
	//returned when a unexpected database error occurs like connection failure or memory overflow etc and a empty map is returned 
	//as reason
	public static final int DATA_LAYER_ERROR = 103;
	
	//returned when sent credential like userId, password does not match for authentication and a map of key value pair is returned
	//where key is the credential name(mostly userId) and value is the sent field value
	public static final int CREDENTIAL_NOT_MATCH_ERROR_CODE = 104;
	
	
	
	/*
	 ERROR MESSAGES SENT IN THE ERROR RESPONSE FOR EASE OF DEVELOPMENT
	*/
	public static final String SIGN_UP_FAILED_MSG = "SIGNUP FAILED";
	public static final String SIGN_IN_FAILED_MSG = "SIGNIN FAILED";
	public static final String FOLLOW_REQUEST_FAILED_MSG = "FOLLOW REQUEST FAILED";


	public static final String CONTENT_LOAD_FAILED_MSG = "UNABLE TO LOAD DATA";
	
	
	
}

package org.roybond007.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.roybond007.model.dto.ErrorResponseBody;

public class MovieEntityNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -7110210570991996158L;

	private final int ERROR_CODE;

	private final String ERROR_MSG;
	
	private final String MOVIE_ID;

	public MovieEntityNotFoundException(int ERROR_CODE, String ERROR_MSG, String movieId) {
		super("USER ENTITY NOT FOUND");
		this.ERROR_CODE = ERROR_CODE;
		this.ERROR_MSG = ERROR_MSG;
		this.MOVIE_ID = movieId;
	}
	
	public ErrorResponseBody getErrorResponseBody() {
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("movieId", MOVIE_ID);
		
		ErrorResponseBody errorResponseBody = new ErrorResponseBody(ERROR_CODE, ERROR_MSG, map);
		
		return errorResponseBody;
	}
	
}

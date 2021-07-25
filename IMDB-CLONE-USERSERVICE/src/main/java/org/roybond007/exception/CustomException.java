package org.roybond007.exception;

import org.roybond007.model.helper.ErrorResponseBody;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class CustomException extends RuntimeException {

	private static final long serialVersionUID = -6157105965244651848L;

	protected int errorCode;
	protected String errorMsg;
	protected Object reason;
	
	public CustomException(int errorCode, String errorMsg, Object reason) {
		super(errorMsg);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.reason = reason;
	}
	
	public abstract ErrorResponseBody getErrorResponseBody();
	
}

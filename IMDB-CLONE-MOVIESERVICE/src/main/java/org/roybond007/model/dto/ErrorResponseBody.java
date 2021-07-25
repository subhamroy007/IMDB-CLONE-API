package org.roybond007.model.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponseBody {
	
	private int code;
	private String msg;
	private Map<String, Object> reason;
	
}


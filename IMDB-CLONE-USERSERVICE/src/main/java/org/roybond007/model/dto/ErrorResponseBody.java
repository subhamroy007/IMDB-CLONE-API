package org.roybond007.model.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponseBody {
	
	private final int code;
	private final String msg;
	private final Map<String, Object> reason;
	
}

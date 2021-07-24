package org.roybond007.model.helper;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponseBody {
	
	private int code;
	private String msg;
	private Map<String, Object> reason;
	
}

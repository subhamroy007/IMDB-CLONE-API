package org.roybond007.model.dto;

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
public class UserAuthenticationResponseBody {

	private String token;
	private String userId;
	private boolean isAdmin;
	
}

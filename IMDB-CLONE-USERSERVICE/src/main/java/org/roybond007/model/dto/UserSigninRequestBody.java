package org.roybond007.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class UserSigninRequestBody {

	@NotNull(message = "userid cannot be empty")
	@Size(min = 10, max = 50, message = "userid length should be within 10 to 50")
	private String userId;
	
	@NotNull(message = "password cannot be empty")
	@Size(min = 10, max = 20, message = "password length should be between 10 to 20")
	private String password;
	
}

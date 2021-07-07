package org.roybond007.controller;

import javax.validation.Valid;

import org.roybond007.exception.CustomValidationException;
import org.roybond007.model.dto.UserSignupRequestBody;
import org.roybond007.model.dto.UserSignupResponseBody;
import org.roybond007.service.UserService;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/account")
public class UserAccountController {

	private final UserService userService;
	
	
	@Autowired
	public UserAccountController(UserService userService) {
		
		this.userService = userService;
	}



	@GetMapping(
				consumes = {MediaType.APPLICATION_JSON_VALUE}, 
				produces = {MediaType.APPLICATION_JSON_VALUE}
				)
	public ResponseEntity<?> doUserSignup(@Valid @RequestBody(required = true) UserSignupRequestBody userSignupRequestBody
			, BindingResult result){
		
		if(result.hasErrors()) {
			System.err.println("something went wrong when validating user signup request body");
			result.getFieldErrors().forEach(error -> {
				System.out.println(error.getField() + "-->" + error.getDefaultMessage());
			});
			throw new CustomValidationException(result.getFieldErrors()
					, ErrorUtility.SIGN_UP_FAILED_CODE
					, ErrorUtility.SIGN_UP_FAILED_MSG);
		}
		
		UserSignupResponseBody userSignupResponseBody = userService.createNewUserEntity(userSignupRequestBody);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userSignupResponseBody);
	}
	
	
}

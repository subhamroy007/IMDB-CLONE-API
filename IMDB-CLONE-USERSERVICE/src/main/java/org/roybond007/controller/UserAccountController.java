package org.roybond007.controller;

import javax.validation.Valid;

import org.roybond007.exception.SignInFailedException;
import org.roybond007.exception.SignUpFailedException;
import org.roybond007.model.dto.UserAuthenticationResponseBody;
import org.roybond007.model.dto.UserSigninRequestBody;
import org.roybond007.model.dto.UserSignupRequestBody;
import org.roybond007.service.UserService;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
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



	@PostMapping(
				consumes = {MediaType.APPLICATION_JSON_VALUE}, 
				produces = {MediaType.APPLICATION_JSON_VALUE},
				value = "/signup"
				)
	public ResponseEntity<?> doUserSignup(@Valid @RequestBody(required = true) UserSignupRequestBody userSignupRequestBody
			, BindingResult result){
		
		if(result.hasErrors()) {
			System.err.println("something went wrong when validating user signup request body");
			result.getFieldErrors().forEach(error -> {
				System.err.println(error.getField() + "-->" + error.getDefaultMessage());
			});
			throw new 
				SignUpFailedException(ErrorUtility.VALIDATION_FAILED_CODE, ErrorUtility.SIGN_UP_FAILED_MSG
						, result.getFieldErrors()
				);
		}
		
		UserAuthenticationResponseBody userAuthenticationResponseBody = userService.createNewUserEntity(userSignupRequestBody);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userAuthenticationResponseBody);
	}
	
	
	@PostMapping(
				consumes = {MediaType.APPLICATION_JSON_VALUE},
				produces = {MediaType.APPLICATION_JSON_VALUE},
				value = "/signin"
				)
	public ResponseEntity<?> doUserSignin(@Valid @RequestBody(required = true) UserSigninRequestBody signupRequestBody
			, BindingResult result){
		
		if(result.hasErrors()) {
			System.err.println("something went wrong when validating user signin request body");
			result.getFieldErrors().forEach(error -> {
				System.err.println(error.getField() + "-->" + error.getDefaultMessage());
			});
			throw new SignInFailedException(ErrorUtility.VALIDATION_FAILED_CODE
					, ErrorUtility.SIGN_IN_FAILED_MSG
					, result.getFieldErrors());
		}
		
		UserAuthenticationResponseBody userAuthenticationResponseBody = userService.authenticateUserEntity(signupRequestBody);
		
		return ResponseEntity.ok(userAuthenticationResponseBody);
	}
	
	
}

package org.roybond007.service;


import org.roybond007.model.dto.UserSignupRequestBody;
import org.roybond007.model.dto.UserSignupResponseBody;

public interface UserService {

	UserSignupResponseBody createNewUserEntity(UserSignupRequestBody userSignupRequestBody);

}

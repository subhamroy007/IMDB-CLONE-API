package org.roybond007.service;


import org.roybond007.model.dto.UserSignupRequestBody;

import javax.validation.Valid;

import org.roybond007.model.dto.EntityListUpdatedResponseBody;
import org.roybond007.model.dto.UserAuthenticationResponseBody;
import org.roybond007.model.dto.UserSigninRequestBody;

public interface UserService {

	UserAuthenticationResponseBody createNewUserEntity(UserSignupRequestBody userSignupRequestBody);

	UserAuthenticationResponseBody authenticateUserEntity(UserSigninRequestBody signinRequestBody);

	EntityListUpdatedResponseBody updateFollowStatus(String currentUserId, String targetUserId);

	EntityListUpdatedResponseBody updateWatchList(String userId, String movieId);

	EntityListUpdatedResponseBody updateWishList(String userId, String movieId);

}

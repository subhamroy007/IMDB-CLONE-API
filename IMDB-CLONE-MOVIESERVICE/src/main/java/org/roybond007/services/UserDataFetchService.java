package org.roybond007.services;

import org.roybond007.model.dto.UserInfoResponseBody;

public interface UserDataFetchService {

	UserInfoResponseBody fetchUserInfo(String userId);
	
	
}

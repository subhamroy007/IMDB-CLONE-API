package org.roybond007.service;

import org.roybond007.model.dto.MoviePageObject;
import org.roybond007.model.dto.UserInfoResponseBody;
import org.roybond007.model.dto.UserProfileInfoResponseBody;

public interface UserDataFetchService {

	MoviePageObject getWishListMovie(String userId, long pageId);

	MoviePageObject getWatchListMovie(String userId, long pageId);

	MoviePageObject getRatingListMovie(String userId, long pageId);

	UserProfileInfoResponseBody getProfileInfo(String sourceId, String targetId);

	UserInfoResponseBody getUserInfo(String userId);
	
}

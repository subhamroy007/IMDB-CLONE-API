package org.roybond007.service;

import org.roybond007.model.dto.MoviePageObject;

public interface UserDataFetchService {

	MoviePageObject getWishListMovie(String userId, long pageId);

	MoviePageObject getWatchListMovie(String userId, long pageId);

	MoviePageObject getRatingListMovie(String userId, long pageId);

}

package org.roybond007.service;

import java.util.ArrayList;

import org.roybond007.exception.ContentLoadFailureException;
import org.roybond007.model.dto.MoviePageObject;
import org.roybond007.model.dto.UserInfoResponseBody;
import org.roybond007.model.dto.UserProfileInfoResponseBody;
import org.roybond007.repository.UserEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class UserDataFetchServiceImpl implements UserDataFetchService {

	@Value("${user.wishlist.pagesize}")
	private long wishListPageSize;
	
	private final UserEntityRepository userEntityRepository;

	@Value("${user.watchlist.pagesize}")
	private long watchListPageSize;
	
	@Value("${user.ratinglist.pagesize}")
	private long ratingListPageSize;
	
	@Autowired
	public UserDataFetchServiceImpl(UserEntityRepository userEntityRepository) {
		this.userEntityRepository = userEntityRepository;
	}
	
	@Override
	public UserInfoResponseBody getUserInfo(String userId) {
		UserInfoResponseBody userInfoResponseBody = null;
		
		try {
			
			userInfoResponseBody = userEntityRepository.findUserInfo(userId);
			
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(userInfoResponseBody == null) {
			throw new ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.CONTENT_LOAD_FAILED_MSG, userId);
		}
		
		String profileLink = ServletUriComponentsBuilder
								.fromCurrentContextPath()
								.path("/" + userInfoResponseBody.getProfilePictureLink())
								.toUriString();

		userInfoResponseBody.setProfilePictureLink(profileLink);
		
		return userInfoResponseBody;
	}
	
	@Override
	public MoviePageObject getWishListMovie(String userId, long pageId) {
		
		MoviePageObject moviePageObject = null;
		
		try {
			moviePageObject = userEntityRepository
								.findWishListMovies(userId, (int)(pageId*wishListPageSize), (int)wishListPageSize);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(moviePageObject == null) {
			System.err.println("userId " + userId + " does not exist");
			throw new ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.CONTENT_LOAD_FAILED_MSG, userId);
		}
		
		moviePageObject.setId(pageId);
		moviePageObject.setSize(wishListPageSize);
		if(moviePageObject.getResult() == null) {
			moviePageObject.setResult(new ArrayList<>());
		}
		moviePageObject.setLength(moviePageObject.getResult().size());
		
		moviePageObject.getResult().forEach(movie -> {

			String posterLink = ServletUriComponentsBuilder
									.fromCurrentContextPath()
									.path("/" + movie.getPosterLink())
									.toUriString();
			
			movie.setPosterLink(posterLink);
		});
		
		return moviePageObject;
	}

	@Override
	public MoviePageObject getWatchListMovie(String userId, long pageId) {
		
		MoviePageObject moviePageObject = null;
		
		try {
			moviePageObject = userEntityRepository
								.findWatchListMovies(userId, (int)(pageId*watchListPageSize), (int)watchListPageSize);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(moviePageObject == null) {
			System.err.println("userId " + userId + " does not exist");
			throw new ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.CONTENT_LOAD_FAILED_MSG, userId);
		}
		
		moviePageObject.setId(pageId);
		moviePageObject.setSize(watchListPageSize);
		if(moviePageObject.getResult() == null) {
			moviePageObject.setResult(new ArrayList<>());
		}
		moviePageObject.setLength(moviePageObject.getResult().size());
		
		moviePageObject.getResult().forEach(movie -> {

			String posterLink = ServletUriComponentsBuilder
									.fromCurrentContextPath()
									.path("/" + movie.getPosterLink())
									.toUriString();
			
			movie.setPosterLink(posterLink);
		});
		
		return moviePageObject;
	}
	
	@Override
	public MoviePageObject getRatingListMovie(String userId, long pageId) {
		
		MoviePageObject moviePageObject = null;
		
		try {
			moviePageObject = userEntityRepository
								.findRatingListMovies(userId, (int)(pageId*ratingListPageSize), (int)ratingListPageSize);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(moviePageObject == null) {
			System.err.println("userId " + userId + " does not exist");
			throw new ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.CONTENT_LOAD_FAILED_MSG, userId);
		}
		
		moviePageObject.setId(pageId);
		moviePageObject.setSize(ratingListPageSize);
		if(moviePageObject.getResult() == null) {
			moviePageObject.setResult(new ArrayList<>());
		}
		moviePageObject.setLength(moviePageObject.getResult().size());
		
		moviePageObject.getResult().forEach(movie -> {

			String posterLink = ServletUriComponentsBuilder
									.fromCurrentContextPath()
									.path("/" + movie.getPosterLink())
									.toUriString();
			
			movie.setPosterLink(posterLink);
		});
		
		return moviePageObject;
	}
	
	@Override
	public UserProfileInfoResponseBody getProfileInfo(String sourceId, String targetId) {
		
		UserProfileInfoResponseBody userProfileInfoResponseBody = null;
		
		try {
			userProfileInfoResponseBody = userEntityRepository.findProfileInfo(sourceId, targetId);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(userProfileInfoResponseBody == null) {
			System.err.println("userId " + targetId + " does not exist");
			throw new ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.CONTENT_LOAD_FAILED_MSG, targetId);
		}
		userProfileInfoResponseBody.setEditable(sourceId.equals(targetId));
		
		String profileLink = ServletUriComponentsBuilder
								.fromCurrentContextPath()
								.path("/" + userProfileInfoResponseBody.getProfilePictureLink())
								.toUriString();

		userProfileInfoResponseBody.setProfilePictureLink(profileLink);
		
		return userProfileInfoResponseBody;
	}
	
}

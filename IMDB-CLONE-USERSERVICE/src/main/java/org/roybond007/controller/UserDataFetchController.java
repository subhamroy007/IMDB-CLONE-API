package org.roybond007.controller;

import javax.servlet.http.HttpServletRequest;

import org.roybond007.model.dto.MoviePageObject;
import org.roybond007.model.dto.UserInfoResponseBody;
import org.roybond007.model.dto.UserProfileInfoResponseBody;
import org.roybond007.service.UserDataFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/data")
public class UserDataFetchController {

	private final UserDataFetchService userDataFetchService;
	
	@Autowired
	public UserDataFetchController(UserDataFetchService userDataFetchService) {
		this.userDataFetchService = userDataFetchService;
	}
	
	@GetMapping(value = "/wishlist", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getWishListMovie(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "pageId") long pageId){
		
		MoviePageObject moviePageObject = userDataFetchService.getWishListMovie(userId, pageId);
		
		return ResponseEntity.ok(moviePageObject);
	}
	
	@GetMapping(value = "/watchlist", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getWatchListMovie(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "pageId") long pageId){
		
		MoviePageObject moviePageObject = userDataFetchService.getWatchListMovie(userId, pageId);
		
		return ResponseEntity.ok(moviePageObject);
	}
	
	@GetMapping(value = "/ratinglist", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getRatingListMovie(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "pageId") long pageId){
		
		MoviePageObject moviePageObject = userDataFetchService.getRatingListMovie(userId, pageId);
		
		return ResponseEntity.ok(moviePageObject);
	}
	
	@GetMapping(value = "/profile", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"userId"})
	public ResponseEntity<?> getProfileInfo(@RequestParam(value = "userId") String userId,
			HttpServletRequest request
	){
		
		String id = "";
		if(request.getUserPrincipal() != null)
			id = request.getUserPrincipal().getName();
		
		UserProfileInfoResponseBody userProfileInfoResponseBody = userDataFetchService.getProfileInfo(id, userId);
		
		MoviePageObject ratingList = userDataFetchService.getRatingListMovie(userId, 0);
		MoviePageObject wishList = userDataFetchService.getWishListMovie(userId, 0);
		MoviePageObject watchList = userDataFetchService.getWatchListMovie(userId, 0);
		
		userProfileInfoResponseBody.setRatingList(ratingList);
		userProfileInfoResponseBody.setWishList(wishList);
		userProfileInfoResponseBody.setWatchList(watchList);
		
		return ResponseEntity.ok(userProfileInfoResponseBody);
	}
	
	@GetMapping(value = "/info", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getUserInfo(HttpServletRequest request
			, @RequestParam(value = "userId", defaultValue = "", required = false) String userId
	){

		if(request.getUserPrincipal() != null && userId.equals(""))
			userId = request.getUserPrincipal().getName();
		
		UserInfoResponseBody body = userDataFetchService.getUserInfo(userId);
		
		return ResponseEntity.ok(body);
	}
}

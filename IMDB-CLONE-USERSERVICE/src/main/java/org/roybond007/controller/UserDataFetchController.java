package org.roybond007.controller;

import org.roybond007.model.dto.MoviePageObject;
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
	
}

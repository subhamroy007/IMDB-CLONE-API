package org.roybond007.controllers;

import javax.servlet.http.HttpServletRequest;

import org.roybond007.model.dto.HomePageResponseBody;
import org.roybond007.model.dto.MovieInfoPageResponseBody;
import org.roybond007.model.dto.MovieInfoResponseBody;
import org.roybond007.model.dto.MovieSearchResponseBody;
import org.roybond007.model.dto.ReplyDataFetchResponseBody;
import org.roybond007.model.dto.ReviewDataFetchResponseBody;
import org.roybond007.model.dto.ReviewPageResponseBody;
import org.roybond007.model.dto.SearchPageResponseBody;
import org.roybond007.model.dto.UserInfoResponseBody;
import org.roybond007.model.helper.ReplyDataFetchObject;
import org.roybond007.model.helper.ReviewDataFetchObject;
import org.roybond007.services.MovieDataFetchService;
import org.roybond007.services.UserDataFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie/fetch")
public class MovieDataFetchController {

	private final MovieDataFetchService movieDataFetchService;
	private final UserDataFetchService userDataFetchService;
	
	@Autowired
	public MovieDataFetchController(MovieDataFetchService movieDataFetchService
			, UserDataFetchService userDataFetchService
	) {
		this.movieDataFetchService = movieDataFetchService;
		this.userDataFetchService = userDataFetchService;
	}
	
	@GetMapping(value = "/info", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"movieId"})
	public ResponseEntity<?> fetchMovieInfo(@RequestParam(name = "movieId", required = true) String movieId
			, HttpServletRequest request){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		
		MovieInfoResponseBody movieInfoResponseBody = movieDataFetchService.fetchMovieInfo(movieId, userId);
		
		UserInfoResponseBody userInfo = null;
		
		if(!userId.equals(""))
			userInfo = userDataFetchService.fetchUserInfo(userId);
		
		MovieInfoPageResponseBody body = new MovieInfoPageResponseBody(userInfo, movieInfoResponseBody);
		
		return ResponseEntity.ok(body);
	}
	
	@GetMapping(value = "/review", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"movieId", "pageId"})
	public ResponseEntity<?> fetchReviewList(@RequestParam(name = "movieId", required = true) String movieId,
			@RequestParam(name = "pageId") long pageId
			, HttpServletRequest request){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		ReviewDataFetchResponseBody reviewDataFetchResponseBody = movieDataFetchService.fetchReviewList(userId, movieId, pageId);
		
		
		return ResponseEntity.ok(reviewDataFetchResponseBody);
	}
	

	@GetMapping(value = "/reply", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"reviewId", "pageId"})
	public ResponseEntity<?> fetchReplyList(@RequestParam(name = "reviewId", required = true) String reviewId,
			@RequestParam(name = "pageId", required = true) long pageId
			, HttpServletRequest request){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		ReplyDataFetchResponseBody replyDataFetchResponseBody = movieDataFetchService.fetchReplyList(userId, reviewId, pageId);
		
		return ResponseEntity.ok(replyDataFetchResponseBody);
	}

	
	@GetMapping(value = "/review", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"reviewId"})
	public ResponseEntity<?> fetchSingleReview(@RequestParam(name = "reviewId", required = true) String reviewId
			, HttpServletRequest request){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		ReviewDataFetchObject reviewDataFetchObject = movieDataFetchService.fetchSingleReview(userId, reviewId);
		
		UserInfoResponseBody userInfo = null;
		
		if(!userId.equals(""))
			userInfo = userDataFetchService.fetchUserInfo(userId);
		
		ReviewPageResponseBody body = new ReviewPageResponseBody(userInfo, reviewDataFetchObject);
		
		return ResponseEntity.ok(body);
	}

	@GetMapping(value = "/reply", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"replyId"})
	public ResponseEntity<?> fetchSingleReply(@RequestParam(name = "replyId", required = true) String replyId
			, HttpServletRequest request){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		ReplyDataFetchObject replyDataFetchObject = movieDataFetchService.fetchSingleReply(userId, replyId);
		
		return ResponseEntity.ok(replyDataFetchObject);
	}
	
	
	@GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"query", "pageId"})
	public ResponseEntity<?> fetchQueryMovies(@RequestParam(value = "query") String query
			, @RequestParam(value = "pageId") long pageId
			, HttpServletRequest request
	){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		
		MovieSearchResponseBody responseBody = movieDataFetchService.fetchQueryMovies(query, pageId, userId);
		
		UserInfoResponseBody userInfo = null;
		
		if(!userId.equals(""))
			userInfo = userDataFetchService.fetchUserInfo(userId);
		
		SearchPageResponseBody body = new SearchPageResponseBody(userInfo, responseBody);
		
		return ResponseEntity.ok(body);
	}
	
	
	@GetMapping(value = "/recent", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"pageId"})
	public ResponseEntity<?> fetchRecentMovies(@RequestParam(value = "pageId") long pageId
			, HttpServletRequest request
	){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		MovieSearchResponseBody body = movieDataFetchService.fetchRecentMovies(pageId, userId);
		
		
		return ResponseEntity.ok(body);
	}
	
	
	@GetMapping(value = "/top", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"pageId"})
	public ResponseEntity<?> fetchTopMovies(@RequestParam(value = "pageId") long pageId
			, HttpServletRequest request
	){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		MovieSearchResponseBody body = movieDataFetchService.fetchTopMovies(pageId, userId);
		
		
		return ResponseEntity.ok(body);
	}
	
	@GetMapping(value = "/least", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"pageId"})
	public ResponseEntity<?> fetchLeastMovies(@RequestParam(value = "pageId") long pageId
			, HttpServletRequest request
	){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		MovieSearchResponseBody body = movieDataFetchService.fetchLeastMovies(pageId, userId);
		
		
		return ResponseEntity.ok(body);
	}
	
	@GetMapping(value = "/home", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getHome(HttpServletRequest request){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		MovieSearchResponseBody recentMovies = movieDataFetchService.fetchRecentMovies(0, userId);
		MovieSearchResponseBody leastMovies = movieDataFetchService.fetchLeastMovies(0, userId);
		MovieSearchResponseBody topMovies = movieDataFetchService.fetchTopMovies(0, userId);
		
		UserInfoResponseBody userInfo = null;
		
		if(!userId.equals(""))
			userInfo = userDataFetchService.fetchUserInfo(userId);
		
		HomePageResponseBody body = new HomePageResponseBody(userInfo, recentMovies, topMovies, leastMovies);
		
		return ResponseEntity.ok(body);
	}
	
}

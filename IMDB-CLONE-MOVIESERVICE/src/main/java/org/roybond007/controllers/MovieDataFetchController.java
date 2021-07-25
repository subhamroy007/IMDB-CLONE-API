package org.roybond007.controllers;

import javax.servlet.http.HttpServletRequest;

import org.roybond007.model.dto.MovieInfoResponseBody;
import org.roybond007.model.dto.ReplyDataFetchResponseBody;
import org.roybond007.model.dto.ReviewDataFetchResponseBody;
import org.roybond007.model.helper.ReplyDataFetchObject;
import org.roybond007.model.helper.ReviewDataFetchObject;
import org.roybond007.services.MovieDataFetchService;
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
	
	
	@Autowired
	public MovieDataFetchController(MovieDataFetchService movieDataFetchService) {
		this.movieDataFetchService = movieDataFetchService;
	}
	
	@GetMapping(value = "/info", produces = {MediaType.APPLICATION_JSON_VALUE}, params = {"movieId"})
	public ResponseEntity<?> fetchMovieInfo(@RequestParam(name = "movieId", required = true) String movieId
			, HttpServletRequest request){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		
		MovieInfoResponseBody movieInfoResponseBody = movieDataFetchService.fetchMovieInfo(movieId, userId);
		
		
		return ResponseEntity.ok(movieInfoResponseBody);
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
		
		return ResponseEntity.ok(reviewDataFetchObject);
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
	
}

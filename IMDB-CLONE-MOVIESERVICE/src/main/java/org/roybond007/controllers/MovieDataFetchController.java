package org.roybond007.controllers;

import javax.servlet.http.HttpServletRequest;

import org.roybond007.model.dto.MovieInfoResponseBody;
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
	public ResponseEntity<?> fetchMovieInfo(@RequestParam(name = "movieId") String movieId, HttpServletRequest request){
		
		String userId = "";
		
		if(request.getUserPrincipal() != null)
			userId = request.getUserPrincipal().getName();
		
		
		MovieInfoResponseBody movieInfoResponseBody = movieDataFetchService.fetchMovieInfo(movieId, userId);
		
		
		return ResponseEntity.ok(movieInfoResponseBody);
	}
	
}

package org.roybond007.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.roybond007.exceptions.CustomValidationException;
import org.roybond007.model.dto.MovieUploadRequestBody;
import org.roybond007.model.dto.MovieUploadResponseBody;
import org.roybond007.model.dto.ReactUploadResponseBody;
import org.roybond007.model.dto.ReplyUploadResponseBody;
import org.roybond007.model.dto.ReviewUploadRequestBody;
import org.roybond007.model.dto.ReviewUploadResponseBody;
import org.roybond007.services.MovieManagmentService;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie/managment")
public class MovieManagmentController {

	private final MovieManagmentService movieManagmentService;
	
	@Autowired
	public MovieManagmentController(MovieManagmentService movieManagmentService) {
		this.movieManagmentService = movieManagmentService;
	}
	
	@PostMapping(value = "/upload", 
			consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> uploadMovie(@Valid @ModelAttribute MovieUploadRequestBody movieUploadRequestBody, 
			BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			System.err.println("something went wrong when validating user signup request body");
			bindingResult.getFieldErrors().forEach(error -> {
				System.err.println(error.getField() + "-->" + error.getDefaultMessage());
			});
			throw new CustomValidationException(bindingResult.getFieldErrors()
					, ErrorUtility.VALIDATION_FAILED_CODE
					, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG);
		}
		
		MovieUploadResponseBody movieUploadResponseBody = movieManagmentService.uploadMovie(movieUploadRequestBody);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(movieUploadResponseBody);
	}
	
	@PostMapping(value = "/review", 
			consumes = {MediaType.APPLICATION_JSON_VALUE}, 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> uploadMovieReview(@Valid @RequestBody(required = true) 
		ReviewUploadRequestBody reviewUploadRequestBody,
		BindingResult bindingResult, @RequestParam(value = "movieId") String movieId,
		HttpServletRequest request){

		if(bindingResult.hasErrors()) {
			System.err.println("something went wrong when validating user signup request body");
			bindingResult.getFieldErrors().forEach(error -> {
				System.err.println(error.getField() + "-->" + error.getDefaultMessage());
			});
			throw new CustomValidationException(bindingResult.getFieldErrors()
					, ErrorUtility.VALIDATION_FAILED_CODE
					, ErrorUtility.CONTENT_UPLOAD_FAILED_MSG);
		}

		String userId = request.getUserPrincipal().getName();

		ReviewUploadResponseBody reviewUploadResponseBody = 
			movieManagmentService.uploadReview(userId, movieId, reviewUploadRequestBody);

		return ResponseEntity.status(HttpStatus.CREATED).body(reviewUploadResponseBody);
	}


	@PostMapping(value = "/reply", 
			consumes = {MediaType.APPLICATION_JSON_VALUE}, 
			produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?>uploadMovieReply(@Valid @RequestBody(required = true) 
		ReviewUploadRequestBody reviewUploadRequestBody,
		BindingResult bindingResult, @RequestParam(value = "reviewId") String reviewId,
		HttpServletRequest request){

		if(bindingResult.hasErrors()) {
			System.err.println("something went wrong when validating user signup request body");
			bindingResult.getFieldErrors().forEach(error -> {
				System.err.println(error.getField() + "-->" + error.getDefaultMessage());
			});
			throw new CustomValidationException(bindingResult.getFieldErrors()
					, ErrorUtility.VALIDATION_FAILED_CODE
					, ErrorUtility.CONTENT_UPLOAD_FAILED_MSG);
		}

		String userId = request.getUserPrincipal().getName();		

		ReplyUploadResponseBody replyUploadResponseBody = movieManagmentService.uploadReply(userId, reviewId, reviewUploadRequestBody);		



		return ResponseEntity.status(HttpStatus.CREATED).body(replyUploadResponseBody);
	}


	@PutMapping(value = "/review/react", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> updateReviewReact(@RequestParam(value = "reviewId") String reviewId,
		HttpServletRequest request){

		String userId = request.getUserPrincipal().getName();
		
		ReactUploadResponseBody reactUploadResponseBody = movieManagmentService.updateReviewReact(userId, reviewId);

		return ResponseEntity.ok(reactUploadResponseBody);
	}


	@PutMapping(value = "/reply/react", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> updateReplyReact(@RequestParam(value = "replyId") String replyId,
		HttpServletRequest request){

		String userId = request.getUserPrincipal().getName();
		
		ReactUploadResponseBody reactUploadResponseBody = movieManagmentService.updateReplyReact(userId, replyId);

		return ResponseEntity.ok(reactUploadResponseBody);
	}

}

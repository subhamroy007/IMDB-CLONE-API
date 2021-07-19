package org.roybond007.controllers;

import javax.validation.Valid;

import org.roybond007.exceptions.CustomValidationException;
import org.roybond007.model.dto.MovieUploadRequestBody;
import org.roybond007.model.dto.MovieUploadResponseBody;
import org.roybond007.services.MovieManagmentService;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
}

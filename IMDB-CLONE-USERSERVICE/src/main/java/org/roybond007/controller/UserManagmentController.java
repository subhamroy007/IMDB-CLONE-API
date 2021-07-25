package org.roybond007.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.roybond007.model.dto.EntityListUpdatedResponseBody;
import org.roybond007.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/managment")
public class UserManagmentController {

	private final UserService userService;

	@Autowired
	public UserManagmentController(UserService userService) {
		this.userService = userService;
	}

	@PutMapping(value = "/followrequest", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> changeFollowStatus(
			@RequestParam(value = "targetUserId", required = true) String targetUserId, HttpServletRequest request) {

		String currentUserId = request.getUserPrincipal().getName();

		EntityListUpdatedResponseBody entityListUpdatedResponseBody = userService.updateFollowStatus(currentUserId,
				targetUserId);

		return ResponseEntity.ok().body(entityListUpdatedResponseBody);
	}

	@PutMapping(value = "/watchlist", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> updateWatchList(@RequestParam(value = "movieId", required = true) String movieId,
			HttpServletRequest request) {

		String userId = request.getUserPrincipal().getName();

		EntityListUpdatedResponseBody entityListUpdatedResponseBody = userService.updateWatchList(userId, movieId);

		return ResponseEntity.ok(entityListUpdatedResponseBody);
	}

	@PutMapping(value = "/watchlist", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> updateWishList(@RequestParam(value = "movieId", required = true) String movieId,
			HttpServletRequest request) {

		String userId = request.getUserPrincipal().getName();

		EntityListUpdatedResponseBody entityListUpdatedResponseBody = userService.updateWishList(userId, movieId);

		return ResponseEntity.ok(entityListUpdatedResponseBody);
	}

	
}

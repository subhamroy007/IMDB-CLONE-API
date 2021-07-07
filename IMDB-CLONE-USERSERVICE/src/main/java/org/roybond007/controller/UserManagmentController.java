package org.roybond007.controller;



import javax.servlet.http.HttpServletRequest;

import org.roybond007.model.dto.FollowStatusResponseBody;
import org.roybond007.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping(
				value = "/followrequest",
				produces = {MediaType.APPLICATION_JSON_VALUE}
				)
	public ResponseEntity<?> changeFollowStatus(@RequestParam(value = "target_user_id", required = true) String targetUserId
												, HttpServletRequest request){
		
		String currentUserId = request.getUserPrincipal().getName();
		
		FollowStatusResponseBody followStatusResponseBody = userService.updateFollowStatus(currentUserId, targetUserId);
		
		return ResponseEntity.ok().body(followStatusResponseBody);
	}
	
	
}

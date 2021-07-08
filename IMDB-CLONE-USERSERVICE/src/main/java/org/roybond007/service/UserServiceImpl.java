package org.roybond007.service;

import java.util.ArrayList;

import org.roybond007.exception.UserCreateException;
import org.roybond007.exception.UserVerificationException;
import org.roybond007.model.dto.UserSignupRequestBody;
import org.roybond007.model.dto.FollowStatusResponseBody;
import org.roybond007.model.dto.UserAuthenticationResponseBody;
import org.roybond007.model.dto.UserSigninRequestBody;
import org.roybond007.model.entity.UserEntity;
import org.roybond007.repository.UserEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.roybond007.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final UserEntityRepository userEntityRepository;
	
	private final JwtUtility jwtUtility;
	
	private final AuthenticationManager authenticationManager;
	
	private final UserDetailsService userDetailsService;
	
	@Autowired
	public UserServiceImpl(PasswordEncoder passwordEncoder, UserEntityRepository userEntityRepository
							, JwtUtility jwtUtility, AuthenticationManager authenticationManager
							, UserDetailsService userDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.userEntityRepository = userEntityRepository;
		this.jwtUtility = jwtUtility;
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	public UserAuthenticationResponseBody createNewUserEntity(UserSignupRequestBody userSignupRequestBody) {
	
		UserEntity userEntity = new UserEntity();
		
		userEntity.setActive(true);
		userEntity.setEmailId(userSignupRequestBody.getEmailId());
		userEntity.setId("User@" + System.currentTimeMillis());
		userEntity.setPassword(passwordEncoder.encode(userSignupRequestBody.getPassword()));
		userEntity.setProfilePictureLink("default.jpg");
		userEntity.setRoles(new String[] {"USER"});
		userEntity.setUserId(userSignupRequestBody.getUserId());
		userEntity.setChatDestination("/topic/Topic@" + System.currentTimeMillis());
		userEntity.setSubscriptionId(System.currentTimeMillis());
		
		UserEntity target = null;
		
		try {
			target = userEntityRepository.save(userEntity);
		} catch (IllegalArgumentException ex) {
			System.err.println(ex.getLocalizedMessage());
			throw new UserCreateException(ErrorUtility.SIGN_UP_FAILED_CODE, ErrorUtility.SIGN_UP_FAILED_MSG);
		} catch (DataAccessException ex) {
			System.err.println(ex.getLocalizedMessage());
			throw new UserCreateException(ErrorUtility.SIGN_UP_FAILED_CODE, ErrorUtility.SIGN_UP_FAILED_MSG);
		}
		
		UserDetails newUser = User.withUsername(target.getUserId())
							.accountExpired(!target.isActive())
							.accountLocked(!target.isActive())
							.credentialsExpired(!target.isActive())
							.disabled(!target.isActive())
							.password(target.getPassword())
							.roles(target.getRoles())
							.build();
		
		UserAuthenticationResponseBody userAuthenticationResponseBody = 
				new UserAuthenticationResponseBody(jwtUtility.generateToken(newUser), userSignupRequestBody.getUserId());
		
		return userAuthenticationResponseBody;
	}

	
	@Override
	public UserAuthenticationResponseBody authenticateUserEntity(UserSigninRequestBody signupRequestBody) {
		
		try {
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(signupRequestBody.getUserId(), 
															signupRequestBody.getPassword(), 
															new ArrayList<>());
			
			authenticationManager.authenticate(authenticationToken);
			
		} catch (BadCredentialsException ex) {
			System.out.println(ex.getLocalizedMessage());
			throw new UserVerificationException(ErrorUtility.SIGN_IN_FAILED_CODE
												, ErrorUtility.SIGN_IN_FAILED_MSG
												, signupRequestBody.getUserId());
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(signupRequestBody.getUserId());
		
		UserAuthenticationResponseBody userAuthenticationResponseBody = 
				new UserAuthenticationResponseBody(jwtUtility.generateToken(userDetails), signupRequestBody.getUserId());
		
		return userAuthenticationResponseBody;
	}
	
	@Override
	public FollowStatusResponseBody updateFollowStatus(String currentUserId, String targetUserId) {
		
		FollowStatusResponseBody followStatusResponseBody = userEntityRepository.updateFollowStatus(currentUserId, targetUserId);
		
		return followStatusResponseBody;
	}

}

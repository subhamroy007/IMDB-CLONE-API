package org.roybond007.service;

import java.util.ArrayList;

import org.roybond007.exception.SignInFailedException;
import org.roybond007.exception.SignUpFailedException;
import org.roybond007.model.dto.UserSignupRequestBody;
import org.roybond007.model.dto.EntityListUpdatedResponseBody;
import org.roybond007.model.dto.UserAuthenticationResponseBody;
import org.roybond007.model.dto.UserSigninRequestBody;
import org.roybond007.model.entity.UserEntity;
import org.roybond007.repository.UserEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.roybond007.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
		userEntity.setProfilePictureLink("profiles/default.jpg");
		userEntity.setRoles(new String[] {"USER"});
		userEntity.setUserId(userSignupRequestBody.getUserId());
		
		UserEntity target = null;
		
		try {
			target = userEntityRepository.save(userEntity);
		} catch (IllegalArgumentException ex) {
			System.err.println(ex.getLocalizedMessage());
			throw new SignUpFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.SIGN_UP_FAILED_MSG, null);
		} catch (DataAccessException ex) {
			System.err.println(ex.getLocalizedMessage());
			throw new SignUpFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.SIGN_UP_FAILED_MSG, null);
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
				new UserAuthenticationResponseBody(jwtUtility.generateToken(newUser)
						, userSignupRequestBody.getUserId()
						, target.getRoles()[0].equals("ADMIN")
				);
		
		return userAuthenticationResponseBody;
	}

	
	@Override
	public UserAuthenticationResponseBody authenticateUserEntity(UserSigninRequestBody signinRequestBody) {
		
		UsernamePasswordAuthenticationToken result = null;
		
		try {
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(signinRequestBody.getUserId(), 
															signinRequestBody.getPassword(), 
															new ArrayList<>());
			
			result = (UsernamePasswordAuthenticationToken)authenticationManager.authenticate(authenticationToken);
			
		} catch (AuthenticationException ex) {
			System.err.println(ex.getLocalizedMessage());
			throw new SignInFailedException(ErrorUtility.CREDENTIAL_NOT_MATCH_ERROR_CODE
												, ErrorUtility.SIGN_IN_FAILED_MSG
												, signinRequestBody.getUserId());
		}

		UserDetails userDetails = null;
		try {
			userDetails = userDetailsService.loadUserByUsername(signinRequestBody.getUserId());
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new SignInFailedException(ErrorUtility.DATA_LAYER_ERROR
					, ErrorUtility.SIGN_IN_FAILED_MSG, null);
		}
		
		UserAuthenticationResponseBody userAuthenticationResponseBody = 
				new UserAuthenticationResponseBody(jwtUtility.generateToken(userDetails)
						, signinRequestBody.getUserId()
						, result.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))
				);
		
		return userAuthenticationResponseBody;
	}
	
	@Override
	public EntityListUpdatedResponseBody updateFollowStatus(String currentUserId, String targetUserId) {
		
		EntityListUpdatedResponseBody entityListUpdatedResponseBody = userEntityRepository.updateFollowStatus(currentUserId, targetUserId);
		
		return entityListUpdatedResponseBody;
	}

	@Override
	public EntityListUpdatedResponseBody updateWatchList(String userId, String movieId) {
		
		EntityListUpdatedResponseBody entityListUpdatedResponseBody = userEntityRepository.updateWatchList(userId, movieId);
		
		return entityListUpdatedResponseBody;
	}

	@Override
	public EntityListUpdatedResponseBody updateWishList(String userId, String movieId) {
		
		EntityListUpdatedResponseBody entityListUpdatedResponseBody = userEntityRepository.updateWishList(userId, movieId);
		
		return entityListUpdatedResponseBody;
	}
	
}

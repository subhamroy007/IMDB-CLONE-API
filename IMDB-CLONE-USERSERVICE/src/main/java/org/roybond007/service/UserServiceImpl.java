package org.roybond007.service;

import org.roybond007.exception.UserCreateException;
import org.roybond007.model.dto.UserSignupRequestBody;
import org.roybond007.model.dto.UserSignupResponseBody;
import org.roybond007.model.entity.UserEntity;
import org.roybond007.repository.UserEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.roybond007.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImpl implements UserService {

	private final PasswordEncoder passwordEncoder;
	
	private final UserEntityRepository userEntityRepository;
	
	private final JwtUtility jwtUtility;
	
	@Autowired
	public UserServiceImpl(PasswordEncoder passwordEncoder, UserEntityRepository userEntityRepository, JwtUtility jwtUtility) {
		this.passwordEncoder = passwordEncoder;
		this.userEntityRepository = userEntityRepository;
		this.jwtUtility = jwtUtility;
	}
	
	@Override
	public UserSignupResponseBody createNewUserEntity(UserSignupRequestBody userSignupRequestBody) {
	
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
		
		UserSignupResponseBody userSignupResponseBody = new UserSignupResponseBody(jwtUtility.generateToken(newUser));
		
		return userSignupResponseBody;
	}



}

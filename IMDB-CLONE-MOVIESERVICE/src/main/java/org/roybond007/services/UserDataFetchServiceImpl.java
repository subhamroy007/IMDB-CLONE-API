package org.roybond007.services;

import org.roybond007.exceptions.ContentLoadFailureException;
import org.roybond007.model.dto.UserInfoResponseBody;
import org.roybond007.repositories.UserEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class UserDataFetchServiceImpl implements UserDataFetchService {

	
	private final UserEntityRepository userEntityRepository;
	
	@Autowired
	public UserDataFetchServiceImpl(UserEntityRepository userEntityRepository) {
		this.userEntityRepository = userEntityRepository;
	}
	
	@Override
	public UserInfoResponseBody fetchUserInfo(String userId) {
		
		UserInfoResponseBody userInfoResponseBody = null;
		
		try {
			
			userInfoResponseBody = userEntityRepository.findUserInfo(userId);
			
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(userInfoResponseBody == null) {
			throw new ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, userId);
		}
		
		String profileLink = ServletUriComponentsBuilder
								.fromCurrentContextPath()
								.path("/" + userInfoResponseBody.getProfilePictureLink())
								.toUriString();
		
		userInfoResponseBody.setProfilePictureLink(profileLink);
		
		return userInfoResponseBody;
	}

}

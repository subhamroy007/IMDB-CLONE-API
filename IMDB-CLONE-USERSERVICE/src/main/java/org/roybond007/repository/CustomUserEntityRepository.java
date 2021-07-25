package org.roybond007.repository;

import org.roybond007.model.dto.EntityListUpdatedResponseBody;

public interface CustomUserEntityRepository {
	
	EntityListUpdatedResponseBody updateFollowStatus(String currentUserId, String targetUserId);
	
	EntityListUpdatedResponseBody updateWishList(String userId, String movieId);
	
	EntityListUpdatedResponseBody updateWatchList(String userId, String movieId);
}

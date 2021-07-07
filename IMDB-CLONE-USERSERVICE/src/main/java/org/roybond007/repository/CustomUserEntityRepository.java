package org.roybond007.repository;

import org.roybond007.model.dto.FollowStatusResponseBody;

public interface CustomUserEntityRepository {
	
	FollowStatusResponseBody updateFollowStatus(String currentUserId, String targetUserId);
}

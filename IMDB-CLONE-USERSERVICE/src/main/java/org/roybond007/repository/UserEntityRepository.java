package org.roybond007.repository;

import java.util.Optional;

import org.roybond007.model.dto.FollowStatusResponseBody;
import org.roybond007.model.entity.UserEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends PagingAndSortingRepository<UserEntity, String>, CustomUserEntityRepository{

	@Query(fields = "{_id: 1, userId: 1, password: 1, emailId: 1, roles: 1, isActive: 1}")
	Optional<UserEntity> findByUserId(String userId);
	
}

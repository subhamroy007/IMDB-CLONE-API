package org.roybond007.repository;

import java.util.Optional;

import org.roybond007.model.entity.UserEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;


@Repository
public interface UserEntityRepository extends ReactiveSortingRepository<UserEntity, String>{

	@Query(fields = "{_id: 1, userId: 1, password: 1, emailId: 1, roles: 1, isActive: 1}")
	Mono<UserEntity> findByUserId(String userId);
	
}

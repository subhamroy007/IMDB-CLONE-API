package org.roybond007.repository;

import org.roybond007.model.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserEntityRepository extends PagingAndSortingRepository<UserEntity, String>{

}

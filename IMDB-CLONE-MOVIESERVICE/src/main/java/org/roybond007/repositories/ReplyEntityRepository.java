package org.roybond007.repositories;

import org.roybond007.model.entity.ReplyEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyEntityRepository extends PagingAndSortingRepository<ReplyEntity, String>
    ,CustomReplyEntityRepository{
    
}

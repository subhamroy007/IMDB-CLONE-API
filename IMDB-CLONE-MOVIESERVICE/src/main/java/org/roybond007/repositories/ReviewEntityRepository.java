package org.roybond007.repositories;

import org.roybond007.model.entity.ReviewEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewEntityRepository extends PagingAndSortingRepository<ReviewEntity, String>,
    CustomReviewEntityRepository{


    
}

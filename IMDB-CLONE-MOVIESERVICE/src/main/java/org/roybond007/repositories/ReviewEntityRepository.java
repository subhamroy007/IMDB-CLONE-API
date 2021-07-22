package org.roybond007.repositories;

import org.roybond007.model.entity.ReviewEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReviewEntityRepository extends PagingAndSortingRepository<ReviewEntity, String>,
    CustomReviewEntityRepository{

    
}

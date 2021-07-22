package org.roybond007.repositories;

import org.roybond007.model.entity.ReviewEntity;

public interface CustomReviewEntityRepository {

    void uploadReviewToUser(ReviewEntity target);

    void uploadReviewToMovie(ReviewEntity target);
    
}

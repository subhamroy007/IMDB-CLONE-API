package org.roybond007.repositories;

import org.roybond007.model.dto.ReactUploadResponseBody;
import org.roybond007.model.entity.ReviewEntity;

public interface CustomReviewEntityRepository {

    void uploadReviewToUser(ReviewEntity target);

    void uploadReviewToMovie(ReviewEntity target);

    ReactUploadResponseBody uploadReacToReview(String userId, String reviewId);

}

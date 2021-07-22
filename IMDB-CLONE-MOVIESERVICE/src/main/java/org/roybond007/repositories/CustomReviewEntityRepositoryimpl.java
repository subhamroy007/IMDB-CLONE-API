package org.roybond007.repositories;

import org.roybond007.exceptions.ReviewUploadFailedException;
import org.roybond007.model.entity.MovieEntity;
import org.roybond007.model.entity.ReviewEntity;
import org.roybond007.model.entity.UserEntity;
import org.roybond007.model.helper.EntityReferenceWithTimestamp;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;
import static org.springframework.data.mongodb.core.query.Criteria.*;



@Repository
public class CustomReviewEntityRepositoryimpl implements CustomReviewEntityRepository{

    private final MongoTemplate mongoTemplate;

    
    @Autowired
    public CustomReviewEntityRepositoryimpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void uploadReviewToUser(ReviewEntity target) {
        
        EntityReferenceWithTimestamp reviewRef = 
            new EntityReferenceWithTimestamp(target.getId(), target.getTimestamp());

        Query uploadReviewToMovieQuery = new Query(where("userId").is(target.getUserId()));
        Update uploadReviewToMovieUpdate = new Update()
                                                    .inc("noOfMovieReviewed", 1)
                                                    .push("reviewList")
                                                    .sort(Sort.by("timestamp").descending())
                                                    .each(reviewRef);


        try {
            UpdateResult updateResult = mongoTemplate
            .update(UserEntity.class)
            .matching(uploadReviewToMovieQuery)
            .apply(uploadReviewToMovieUpdate)
            .first();

            if(!updateResult.wasAcknowledged() || (updateResult.getMatchedCount() != 1))
                throw new ReviewUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE,
                    ErrorUtility.CONTENT_UPLOAD_FAILED_MSG);

        } catch (DataAccessException e) {
            System.err.println(e.getLocalizedMessage());
            throw new ReviewUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE,
                    ErrorUtility.CONTENT_UPLOAD_FAILED_MSG);
        }
        
    }

    @Override
    public void uploadReviewToMovie(ReviewEntity target) {

        EntityReferenceWithTimestamp reviewRef = 
            new EntityReferenceWithTimestamp(target.getId(), target.getTimestamp());

        Query uploadReviewToMovieQuery = new Query(where("_id").is(target.getMovieId()));
        Update uploadReviewToMovieUpdate = new Update()
                                                    .inc("noOfReviews", 1)
                                                    .push("reviewList")
                                                    .sort(Sort.by("timestamp").descending())
                                                    .each(reviewRef);


        try {
            UpdateResult updateResult = mongoTemplate
            .update(MovieEntity.class)
            .matching(uploadReviewToMovieQuery)
            .apply(uploadReviewToMovieUpdate)
            .first();

            if(!updateResult.wasAcknowledged() || (updateResult.getMatchedCount() != 1))
                throw new ReviewUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE,
                    ErrorUtility.CONTENT_UPLOAD_FAILED_MSG);

        } catch (DataAccessException e) {
            System.err.println(e.getLocalizedMessage());
            throw new ReviewUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE,
                    ErrorUtility.CONTENT_UPLOAD_FAILED_MSG);
        }

    }

    
    
}

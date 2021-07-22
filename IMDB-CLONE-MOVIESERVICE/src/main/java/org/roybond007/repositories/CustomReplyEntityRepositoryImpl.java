package org.roybond007.repositories;

import org.roybond007.exceptions.ReviewUploadFailedException;
import org.roybond007.model.entity.ReplyEntity;
import org.roybond007.model.entity.ReviewEntity;
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
public class CustomReplyEntityRepositoryImpl implements CustomReplyEntityRepository{
   
    private final MongoTemplate mongoTemplate;

    
    @Autowired
    public CustomReplyEntityRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }



    @Override
    public void uploadReplyToReview(ReplyEntity target) {
        
        EntityReferenceWithTimestamp replyRef = 
            new EntityReferenceWithTimestamp(target.getId(), target.getTimestamp());

        Query uploadReplyToReviewQuery = new Query(where("_id").is(target.getReviewId()));

        Update uploadReplyToReviewUpdate = new Update()
                                                    .inc("noOfReplies", 1)
                                                    .push("replyList")
                                                    .sort(Sort.by("timestamp").descending())
                                                    .each(replyRef);

        try {
            UpdateResult updateResult = mongoTemplate
                                        .update(ReviewEntity.class)
                                        .matching(uploadReplyToReviewQuery)
                                        .apply(uploadReplyToReviewUpdate)
                                        .first();

            if(!updateResult.wasAcknowledged() || (updateResult.getMatchedCount() != 1)){
                throw new ReviewUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, 
				ErrorUtility.CONTENT_UPLOAD_FAILED_MSG);
            }
                                    
        } catch (DataAccessException e) {
            System.err.println(e.getLocalizedMessage());
            throw new ReviewUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, 
				ErrorUtility.CONTENT_UPLOAD_FAILED_MSG);
        }

    }
}

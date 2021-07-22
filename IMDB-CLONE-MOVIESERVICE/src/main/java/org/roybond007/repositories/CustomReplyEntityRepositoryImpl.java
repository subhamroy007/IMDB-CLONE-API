package org.roybond007.repositories;

import org.roybond007.exceptions.ReactionUploadFailedException;
import org.roybond007.exceptions.ReviewUploadFailedException;
import org.roybond007.model.dto.ReactUploadResponseBody;
import org.roybond007.model.entity.ReplyEntity;
import org.roybond007.model.entity.ReviewEntity;
import org.roybond007.model.helper.EntityReferenceWithTimestamp;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;
import static org.springframework.data.mongodb.core.query.Criteria.*;

import java.util.Optional;

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

    @Override
    public ReactUploadResponseBody uploadReacToReply(String userId, String replyId) {
        ReactUploadResponseBody reactUploadResponseBody = new ReactUploadResponseBody();
        reactUploadResponseBody.setId(replyId);
        reactUploadResponseBody.setUserId(userId);

        Query reactToReviewQuery = new Query(where("_id").is(replyId));

        reactToReviewQuery.fields()
            .include("_id", "noOfLikes")
            .elemMatch("likeList", where("id").is(userId));


        Optional<ReplyEntity> target = null;

        try {
            target = mongoTemplate
                        .query(ReplyEntity.class)
                        .as(ReplyEntity.class)
                        .matching(reactToReviewQuery)
                        .first();            
        } catch (DataAccessException e) {
            System.err.println(e.getLocalizedMessage());
            throw new ReactionUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE,
                ErrorUtility.REACTION_UPDATE_FAILED_MSG);
        }        

        if(target.isEmpty())
            throw new ReactionUploadFailedException(ErrorUtility.ENTITY_NOT_FOUND_CODE,
                ErrorUtility.REACTION_UPDATE_FAILED_MSG);

        ReplyEntity replyEntity = target.get();

        if(replyEntity.getLikeList() == null || replyEntity.getLikeList().size() == 0){
            
            EntityReferenceWithTimestamp likeRef = new EntityReferenceWithTimestamp(userId, System.currentTimeMillis());

            Update reactToReviewUpdate = new Update()
                                                .inc("noOfLikes", 1)
                                                .push("likeList")
                                                .sort(Sort.by("timestamp").descending())
                                                .each(likeRef);
            
            Optional<ReplyEntity> result = null;

            try {
                result = mongoTemplate
                            .update(ReplyEntity.class)
                            .matching(reactToReviewQuery)
                            .apply(reactToReviewUpdate)
                            .withOptions(FindAndModifyOptions.options().returnNew(true))
                            .findAndModify();
            } catch (DataAccessException e) {
                System.err.println(e.getLocalizedMessage());
                throw new ReactionUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE,
                ErrorUtility.REACTION_UPDATE_FAILED_MSG);
            }
            
            reactUploadResponseBody.setStatus(1);
            reactUploadResponseBody.setTimestamp(likeRef.getTimestamp());
            reactUploadResponseBody.setNoOfLikes(result.get().getNoOfLikes());

        }else{
            EntityReferenceWithTimestamp likeRef = replyEntity.getLikeList().get(0);

            Update reactToReviewUpdate = new Update()
                                                .inc("noOfLikes", -1)
                                                .pull("likeList", likeRef);
            
            Optional<ReplyEntity> result = null;

            try {
                result = mongoTemplate
                            .update(ReplyEntity.class)
                            .matching(reactToReviewQuery)
                            .apply(reactToReviewUpdate)
                            .withOptions(FindAndModifyOptions.options().returnNew(true))
                            .findAndModify();
            } catch (DataAccessException e) {
                System.err.println(e.getLocalizedMessage());
                throw new ReactionUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE,
                ErrorUtility.REACTION_UPDATE_FAILED_MSG);
            }

            reactUploadResponseBody.setStatus(0);
            reactUploadResponseBody.setTimestamp(-1);
            reactUploadResponseBody.setNoOfLikes(result.get().getNoOfLikes());
        }

        return reactUploadResponseBody;
    }
}

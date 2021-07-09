package org.roybond007.repository;

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

import org.roybond007.exception.UserEntityNotFoundException;
import org.roybond007.exception.UserEntityUpdateFailedException;
import org.roybond007.model.dto.FollowStatusResponseBody;
import org.roybond007.model.entity.UserEntity;
import org.roybond007.model.helper.EntityReferenceWithTimestamp;
import org.roybond007.utils.ErrorUtility;

@Repository
public class CustomUserEntityRepositoryImpl implements CustomUserEntityRepository {

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public CustomUserEntityRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public FollowStatusResponseBody updateFollowStatus(String currentUserId, String targetUserId) {
		
		FollowStatusResponseBody followStatusResponseBody = new FollowStatusResponseBody();
		followStatusResponseBody.setFollowerId(currentUserId);
		followStatusResponseBody.setFollowingId(targetUserId);
		
		Query fetchCurrentUserEntityQuery = new Query(where("userId").is(currentUserId));
		
		fetchCurrentUserEntityQuery
			.fields()
			.include("_id", "userId", "noOfFollowings")
			.elemMatch("followingList", where("_id").is(targetUserId));
		
		Query fetchTargetUserEntityQuery = new Query(where("userId").is(targetUserId));
		fetchTargetUserEntityQuery
			.fields()
			.include("noOfFollowers");
		
		Optional<UserEntity> result = null;
		
		try {
			
			result = mongoTemplate
				.query(UserEntity.class)
				.as(UserEntity.class)
				.matching(fetchCurrentUserEntityQuery)
				.first();
			
			
			
		}catch (IllegalArgumentException ex) {
			System.err.println(ex.getLocalizedMessage());
			throw new UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG);
			
		}catch (DataAccessException ex) {
			System.err.println(ex.getLocalizedMessage());
			throw new UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG);
		}
		
		if(result.isEmpty()) { //this conditional block runs if the current provided user does not exist
			System.err.println("userid " + currentUserId + " not fouund");
			throw new UserEntityNotFoundException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, currentUserId);
		}
		
		UserEntity target = result.get();
		
		if(target.getFollowingList() == null || target.getFollowingList().size() == 0) { //this conditional block runs only when current user does not follow target user
			
			followStatusResponseBody.setStatus(true);
			
			long timestamp = System.currentTimeMillis();
			
			EntityReferenceWithTimestamp following = new EntityReferenceWithTimestamp(targetUserId, timestamp);
			EntityReferenceWithTimestamp follower = new EntityReferenceWithTimestamp(currentUserId, timestamp);
			
			Update currentUserEntityFollowStatusUpdate = new Update()
																.inc("noOfFollowings", 1)
																.push("followingList")
																.sort(Sort.by("timestamp").descending())
																.each(following);
			Update targetUserEntityFollowStatusUpdate = new Update()
																.inc("noOfFollowers", 1)
																.push("followerList")
																.sort(Sort.by("timestamp").descending())
																.each(follower);
			
			UpdateResult currentUserEntityFollowStatusUpdateResult = null;
			Optional<UserEntity> targetUserEntityFollowStatusUserEntity = null;
			
			try {
				currentUserEntityFollowStatusUpdateResult = mongoTemplate
					.update(UserEntity.class)
					.matching(fetchCurrentUserEntityQuery)
					.apply(currentUserEntityFollowStatusUpdate)
					.first();
				if(!currentUserEntityFollowStatusUpdateResult.wasAcknowledged()) {
					System.err.println("database acknoledgement failed in follow request");
					throw new UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG);
				}
				
			}catch (DataAccessException ex) {
				System.err.println(ex.getLocalizedMessage());
				throw new UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG);
			}
			
			
			try {
				targetUserEntityFollowStatusUserEntity = mongoTemplate
					.update(UserEntity.class)
					.matching(fetchTargetUserEntityQuery)
					.apply(targetUserEntityFollowStatusUpdate)
					.withOptions(FindAndModifyOptions.options().returnNew(true).upsert(false))
					.findAndModify();
				
				if(targetUserEntityFollowStatusUserEntity.isEmpty()) {
					System.err.println("userid " + targetUserId + " not found");
					throw new UserEntityNotFoundException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, targetUserId);
				}
				
				followStatusResponseBody.setNoOfFollowers(targetUserEntityFollowStatusUserEntity.get().getNoOfFollowers());
				
			}catch (DataAccessException ex) {
				System.err.println(ex.getLocalizedMessage());
				throw new UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG);
			}
			
			
		}else {	//this block runs when the current user does follow target user
			
			followStatusResponseBody.setStatus(false);
			
			EntityReferenceWithTimestamp following = target.getFollowingList().get(0);
			
			EntityReferenceWithTimestamp follower = new EntityReferenceWithTimestamp(currentUserId, following.getTimestamp());
			
			Update currentUserEntityFollowStatusUpdate = new Update()
																.inc("noOfFollowings", -1)
																.pull("followingList", following);
			
			Update targetUserEntityFollowStatusUpdate = new Update()
																.inc("noOfFollowers", -1)
																.pull("followerList", follower);
			
			UpdateResult currentUserEntityFollowStatusUpdateResult = null;
			Optional<UserEntity> targetUserEntityFollowStatusUserEntity = null;
			
			try {
				currentUserEntityFollowStatusUpdateResult = mongoTemplate
					.update(UserEntity.class)
					.matching(fetchCurrentUserEntityQuery)
					.apply(currentUserEntityFollowStatusUpdate)
					.first();
				if(!currentUserEntityFollowStatusUpdateResult.wasAcknowledged()) {
					System.err.println("database acknoledgement failed in follow request");
					throw new UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG);
				}
				
			}catch (DataAccessException ex) {
				System.err.println(ex.getLocalizedMessage());
				throw new UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG);
			}
			
			
			try {
				targetUserEntityFollowStatusUserEntity = mongoTemplate
					.update(UserEntity.class)
					.matching(fetchTargetUserEntityQuery)
					.apply(targetUserEntityFollowStatusUpdate)
					.withOptions(FindAndModifyOptions.options().returnNew(true).upsert(false))
					.findAndModify();
				
				followStatusResponseBody.setNoOfFollowers(targetUserEntityFollowStatusUserEntity.get().getNoOfFollowers());
				
			}catch (DataAccessException ex) {
				System.err.println(ex.getLocalizedMessage());
				throw new UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG);
			}
			
		}
		
		return followStatusResponseBody;
	}
	
}

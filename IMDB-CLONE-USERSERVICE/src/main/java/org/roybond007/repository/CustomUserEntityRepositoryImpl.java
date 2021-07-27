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
import org.roybond007.exception.UserEntityUpdateFailedException;
import org.roybond007.model.dto.EntityListUpdatedResponseBody;
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
	public EntityListUpdatedResponseBody updateFollowStatus(String currentUserId, String targetUserId) {
		
		EntityListUpdatedResponseBody entityListUpdatedResponseBody = new EntityListUpdatedResponseBody();
		entityListUpdatedResponseBody.setSourceId(currentUserId);
		entityListUpdatedResponseBody.setTargetId(targetUserId);
		
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
			throw new 
				UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, null);
			
		}catch (DataAccessException ex) {
			System.err.println(ex.getLocalizedMessage());
			throw new 
				UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, null);
		}
		
		if(result.isEmpty()) { //this conditional block runs if the current provided user does not exist
			System.err.println("userid " + currentUserId + " not fouund");
			throw new 
				UserEntityUpdateFailedException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, currentUserId);
		}
		
		UserEntity target = result.get();
		
		if(target.getFollowingList() == null || target.getFollowingList().size() == 0) { //this conditional block runs only when current user does not follow target user
			
			entityListUpdatedResponseBody.setStatus(true);
			
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
				targetUserEntityFollowStatusUserEntity = mongoTemplate
					.update(UserEntity.class)
					.matching(fetchTargetUserEntityQuery)
					.apply(targetUserEntityFollowStatusUpdate)
					.withOptions(FindAndModifyOptions.options().returnNew(true).upsert(false))
					.findAndModify();
				
				if(targetUserEntityFollowStatusUserEntity.isEmpty()) {
					System.err.println("userid " + targetUserId + " not found");
					throw new 
						UserEntityUpdateFailedException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, targetUserId);
				}
				
				entityListUpdatedResponseBody.setSize(targetUserEntityFollowStatusUserEntity.get().getNoOfFollowers());
				
			}catch (DataAccessException ex) {
				System.err.println(ex.getLocalizedMessage());
				throw new 
					UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, null);
			}
			
			
			try {
				currentUserEntityFollowStatusUpdateResult = mongoTemplate
					.update(UserEntity.class)
					.matching(fetchCurrentUserEntityQuery)
					.apply(currentUserEntityFollowStatusUpdate)
					.first();
				if(!currentUserEntityFollowStatusUpdateResult.wasAcknowledged()) {
					System.err.println("database acknoledgement failed in follow request");
					throw new 
						UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, null);
				}
				
			}catch (DataAccessException ex) {
				System.err.println(ex.getLocalizedMessage());
				throw new 
					UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, null);
			}
			
			
			
		}else {	//this block runs when the current user does follow target user
			
			entityListUpdatedResponseBody.setStatus(false);
			
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
					throw new 
						UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, null);
				}
				
			}catch (DataAccessException ex) {
				System.err.println(ex.getLocalizedMessage());
				throw new 
					UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, null);
			}
			
			
			try {
				targetUserEntityFollowStatusUserEntity = mongoTemplate
					.update(UserEntity.class)
					.matching(fetchTargetUserEntityQuery)
					.apply(targetUserEntityFollowStatusUpdate)
					.withOptions(FindAndModifyOptions.options().returnNew(true).upsert(false))
					.findAndModify();
				
				entityListUpdatedResponseBody.setSize(targetUserEntityFollowStatusUserEntity.get().getNoOfFollowers());
				
			}catch (DataAccessException ex) {
				System.err.println(ex.getLocalizedMessage());
				throw new 
					UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.FOLLOW_REQUEST_FAILED_MSG, null);
			}
			
		}
		
		return entityListUpdatedResponseBody;
	}

	@Override
	public EntityListUpdatedResponseBody updateWishList(String userId, String movieId) {
		
		EntityListUpdatedResponseBody entityListUpdatedResponseBody = new EntityListUpdatedResponseBody();
		entityListUpdatedResponseBody.setSourceId(userId);
		entityListUpdatedResponseBody.setTargetId(movieId);
		
		
		Query userWishListQuery = new Query(where("userId").is(userId));
		
		userWishListQuery
			.fields()
			.include("_id", "userId", "wishListLength")
			.elemMatch("wishList", where("_id").is(movieId));
		
		Optional<UserEntity> result = null;
		
		try {
			result = mongoTemplate
				.query(UserEntity.class)
				.as(UserEntity.class)
				.matching(userWishListQuery)
				.first();
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new 
				UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.WISHLIST_UPDATE_FAILED_MSG, null);
		}
		
		if(result.isEmpty()) {
			System.err.println("userId " + userId + " does not exists");
			throw new 
				UserEntityUpdateFailedException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.WISHLIST_UPDATE_FAILED_MSG, userId);
		}
		
		UserEntity userEntity = result.get();
		
		if(userEntity.getWishList() == null || userEntity.getWishList().size() == 0) {
			
			EntityReferenceWithTimestamp wishListRef = new EntityReferenceWithTimestamp(movieId, System.currentTimeMillis());
			
			Update userWishListUpdate = new Update()
												.inc("wishListLength", 1)
												.push("wishList")
												.sort(Sort.by("timestamp").descending())
												.each(wishListRef);
			
			try {
				Optional<UserEntity> target = mongoTemplate
												.update(UserEntity.class)
												.matching(userWishListQuery)
												.apply(userWishListUpdate)
												.withOptions(FindAndModifyOptions.options().returnNew(true))
												.findAndModify();

				entityListUpdatedResponseBody.setStatus(true);
				entityListUpdatedResponseBody.setSize(target.get().getWishListLength());
			
			} catch (DataAccessException e) {
				System.err.println(e.getLocalizedMessage());
				throw new 
					UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.WISHLIST_UPDATE_FAILED_MSG, null);
			}
			
			
		}else {
			
			EntityReferenceWithTimestamp wishListRef = userEntity.getWishList().get(0);
			
			Update userWishListUpdate = new Update()
												.inc("wishListLength", -1)
												.pull("wishList", wishListRef);
			
			try {
				
				Optional<UserEntity> target = mongoTemplate
												.update(UserEntity.class)
												.matching(userWishListQuery)
												.apply(userWishListUpdate)
												.withOptions(FindAndModifyOptions.options().returnNew(true))
												.findAndModify();
				
				entityListUpdatedResponseBody.setStatus(false);
				entityListUpdatedResponseBody.setSize(target.get().getWishListLength());
				
			} catch (DataAccessException e) {
				System.err.println(e.getLocalizedMessage());
				throw new 
					UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.WISHLIST_UPDATE_FAILED_MSG, null);
			}
			
		}
		
		return entityListUpdatedResponseBody;
	}
	
	@Override
	public EntityListUpdatedResponseBody updateWatchList(String userId, String movieId) {
		EntityListUpdatedResponseBody entityListUpdatedResponseBody = new EntityListUpdatedResponseBody();
		entityListUpdatedResponseBody.setSourceId(userId);
		entityListUpdatedResponseBody.setTargetId(movieId);
		
		
		Query userWatchListQuery = new Query(where("userId").is(userId));
		
		userWatchListQuery
			.fields()
			.include("_id", "userId", "watchListLength")
			.elemMatch("watchList", where("_id").is(movieId));
		
		Optional<UserEntity> result = null;
		
		try {
			result = mongoTemplate
				.query(UserEntity.class)
				.as(UserEntity.class)
				.matching(userWatchListQuery)
				.first();
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new 
				UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.WISHLIST_UPDATE_FAILED_MSG, null);
		}
		
		if(result.isEmpty()) {
			System.err.println("userId " + userId + " does not exists");
			throw new 
				UserEntityUpdateFailedException(ErrorUtility.ENTITY_NOT_FOUND, ErrorUtility.WISHLIST_UPDATE_FAILED_MSG, userId);
		}
		
		UserEntity userEntity = result.get();
		
		if(userEntity.getWatchList() == null || userEntity.getWatchList().size() == 0) {
			
			EntityReferenceWithTimestamp watchListRef = new EntityReferenceWithTimestamp(movieId, System.currentTimeMillis());
			
			Update userWatchListUpdate = new Update()
												.inc("watchListLength", 1)
												.push("watchList")
												.sort(Sort.by("timestamp").descending())
												.each(watchListRef);
			
			try {
				Optional<UserEntity> target = mongoTemplate
												.update(UserEntity.class)
												.matching(userWatchListQuery)
												.apply(userWatchListUpdate)
												.withOptions(FindAndModifyOptions.options().returnNew(true))
												.findAndModify();

				entityListUpdatedResponseBody.setStatus(true);
				entityListUpdatedResponseBody.setSize(target.get().getWatchListLength());
			
			} catch (DataAccessException e) {
				System.err.println(e.getLocalizedMessage());
				throw new 
					UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.WISHLIST_UPDATE_FAILED_MSG, null);
			}
			
			
		}else {
			
			EntityReferenceWithTimestamp watchListRef = userEntity.getWatchList().get(0);
			
			Update userWatchListUpdate = new Update()
												.inc("watchListLength", -1)
												.pull("watchList", watchListRef);
			
			try {
				
				Optional<UserEntity> target = mongoTemplate
												.update(UserEntity.class)
												.matching(userWatchListQuery)
												.apply(userWatchListUpdate)
												.withOptions(FindAndModifyOptions.options().returnNew(true))
												.findAndModify();
				
				entityListUpdatedResponseBody.setStatus(false);
				entityListUpdatedResponseBody.setSize(target.get().getWatchListLength());
				
			} catch (DataAccessException e) {
				System.err.println(e.getLocalizedMessage());
				throw new 
					UserEntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR, ErrorUtility.WISHLIST_UPDATE_FAILED_MSG, null);
			}
			
		}
		
		return entityListUpdatedResponseBody;
	}
}

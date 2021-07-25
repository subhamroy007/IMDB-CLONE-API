package org.roybond007.repositories;

import org.roybond007.exceptions.EntityUpdateFailedException;
import org.roybond007.model.dto.RatingUploadRequestBody;
import org.roybond007.model.dto.RatingUploadResponseBody;
import org.roybond007.model.entity.MovieEntity;
import org.roybond007.model.entity.UserEntity;
import org.roybond007.model.helper.RatingReference;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import static org.springframework.data.mongodb.core.query.Criteria.*;

import java.util.Optional;


@Repository
public class CustomMovieEntityRepositoryImpl implements CustomMovieEntityRepository {

	private final MongoTemplate mongoTemplate;
	
	@Autowired
	public CustomMovieEntityRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public RatingUploadResponseBody uploadRatingToMovie(RatingUploadRequestBody ratingUploadRequestBody, String userId,
			String movieId) {
		
		long timestamp = System.currentTimeMillis();
		
		RatingUploadResponseBody ratingUploadResponseBody = new RatingUploadResponseBody();
		ratingUploadResponseBody.setMovieId(movieId);
		ratingUploadResponseBody.setUserId(userId);
		ratingUploadResponseBody.setTimestamp(timestamp);
		ratingUploadResponseBody.setRating(ratingUploadRequestBody.getRating());
		
		Query uploadRatingToMovieQuery = new Query(where("_id").is(movieId));
		
		uploadRatingToMovieQuery
			.fields()
			.include("_id", "noOfRatings", "totalRating")
			.elemMatch("ratingList", where("_id").is(userId));
		
		Optional<MovieEntity> result = null;
		
		try {
			result = mongoTemplate
						.query(MovieEntity.class)
						.matching(uploadRatingToMovieQuery)
						.first();
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new 
				EntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.RATING_UPLOAD_FAILED_MSG, null);
			
		}
		
		if(result.isEmpty()) {
			System.err.println("movieId " + movieId + " does not exists");
			throw new
				EntityUpdateFailedException(ErrorUtility.ENTITY_NOT_FOUND_CODE, ErrorUtility.RATING_UPLOAD_FAILED_MSG, movieId);
		}
		
		MovieEntity movieEntity = result.get();
		
		if(movieEntity.getRatingList() == null || movieEntity.getRatingList().size() == 0) {
			
			RatingReference movieRatingRef = 
					new RatingReference(userId, ratingUploadRequestBody.getRating(),timestamp);
			
			
			
			Update uploadRatingToMovieUpdate = new Update()
													.inc("noOfRatings", 1)
													.inc("totalRating", movieRatingRef.getRating())
													.push("ratingList")
													.sort(Sort.by("timestamp").descending())
													.each(movieRatingRef);
			
			Optional<MovieEntity> target = null;
			try {
				
				target = mongoTemplate
							.update(MovieEntity.class)
							.matching(uploadRatingToMovieQuery)
							.apply(uploadRatingToMovieUpdate)
							.withOptions(FindAndModifyOptions.options().returnNew(true))
							.findAndModify();
				
			} catch (DataAccessException e) {
				System.err.println(e.getLocalizedMessage());
				throw new 
					EntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.RATING_UPLOAD_FAILED_MSG, null);
			}
			
			ratingUploadResponseBody.setNoOfRatings(target.get().getNoOfRatings());
			ratingUploadResponseBody.setAvgRating((double)target.get().getTotalRating()/target.get().getNoOfRatings());
			
			try {
				uploadRatingToUser(ratingUploadResponseBody, false, null);
			} catch (DataAccessException e) {
				throw e;
			}
			
			
		}else {
			
			RatingReference oldMovieRatingRef = movieEntity.getRatingList().get(0);
			RatingReference newMovieRatingRef = 
					new RatingReference(movieId, ratingUploadRequestBody.getRating(), timestamp);
			
			
			Update uploadRatingToMovieUpdate = new Update()
					.inc("totalRating", (newMovieRatingRef.getRating()-oldMovieRatingRef.getRating()))
					.pull("ratinglist", oldMovieRatingRef)
					.push("ratingList")
					.sort(Sort.by("timestamp").descending())
					.each(newMovieRatingRef);
			
			
			Optional<MovieEntity> target = null;
			try {
				
				target = mongoTemplate
							.update(MovieEntity.class)
							.matching(uploadRatingToMovieQuery)
							.apply(uploadRatingToMovieUpdate)
							.withOptions(FindAndModifyOptions.options().returnNew(true))
							.findAndModify();
				
			} catch (DataAccessException e) {
				System.err.println(e.getLocalizedMessage());
				throw new
					EntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.RATING_UPLOAD_FAILED_MSG, null);
			}
			
			ratingUploadResponseBody.setNoOfRatings(target.get().getNoOfRatings());
			ratingUploadResponseBody.setAvgRating((double)target.get().getTotalRating()/target.get().getNoOfRatings());
			
			try {
				uploadRatingToUser(ratingUploadResponseBody, true, oldMovieRatingRef);
			} catch (DataAccessException e) {
				throw e;
			}
			
		}
		
		
		
		return ratingUploadResponseBody;
	}

	private void uploadRatingToUser(RatingUploadResponseBody ratingUploadResponseBody, boolean isUpdated, 
			RatingReference oldMovieRatingRef) {

		Query uploadRatingToUserQuery = new Query(where("userId").is(ratingUploadResponseBody.getUserId()));
		
		uploadRatingToUserQuery
			.fields()
			.include("_id", "userId", "noOfMovieRated")
			.elemMatch("ratingList", where("_id").is(ratingUploadResponseBody.getMovieId()));
		
		
		if(!isUpdated) {
			
			RatingReference userRatingRef = 
					new RatingReference(ratingUploadResponseBody.getMovieId(), 
					ratingUploadResponseBody.getRating(), ratingUploadResponseBody.getTimestamp()
					);
			
			Update updateRatingToUserUpdate = new Update()
													.inc("noOfMovieRated", 1)
													.push("ratingList")
													.sort(Sort.by("timestamp").descending())
													.each(userRatingRef);
			
			Optional<UserEntity> target = null;
			
			try {
				
				target = mongoTemplate
							.update(UserEntity.class)
							.matching(uploadRatingToUserQuery)
							.apply(updateRatingToUserUpdate)
							.withOptions(FindAndModifyOptions.options().returnNew(true))
							.findAndModify();
				
			} catch (DataAccessException e) {
				System.err.println(e.getLocalizedMessage());
				throw new
					EntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.RATING_UPLOAD_FAILED_MSG, null);
			}
			
			if(target.isEmpty()) {
				System.err.println("userId " + ratingUploadResponseBody.getUserId() + " does not exist");
				throw new
					EntityUpdateFailedException(ErrorUtility.ENTITY_NOT_FOUND_CODE, ErrorUtility.RATING_UPLOAD_FAILED_MSG
							, ratingUploadResponseBody.getUserId());
			}
			
		}else {
			RatingReference newUserRatingRef = 
					new RatingReference(ratingUploadResponseBody.getMovieId(), 
					ratingUploadResponseBody.getRating(), ratingUploadResponseBody.getTimestamp()
					);
			
			RatingReference oldUserRatingRef = 
					new RatingReference(ratingUploadResponseBody.getMovieId(),
					oldMovieRatingRef.getRating(), oldMovieRatingRef.getTimestamp()
					);
			
			Update updateRatingToUserUpdate = new Update()
					.pull("ratingList", oldUserRatingRef)
					.push("ratingList")
					.sort(Sort.by("timestamp").descending())
					.each(newUserRatingRef);

			Optional<UserEntity> target = null;
			
			try {
			
				target = mongoTemplate
							.update(UserEntity.class)
							.matching(uploadRatingToUserQuery)
							.apply(updateRatingToUserUpdate)
							.withOptions(FindAndModifyOptions.options().returnNew(true))
							.findAndModify();
			
			} catch (DataAccessException e) {
				System.err.println(e.getLocalizedMessage());
				throw new 
					EntityUpdateFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.RATING_UPLOAD_FAILED_MSG, null);
			}
			
			
		}
		
		
	}

}

package org.roybond007.services;

import java.util.Optional;

import org.roybond007.exceptions.ContentLoadFailureException;
import org.roybond007.model.dto.MovieInfoResponseBody;
import org.roybond007.model.dto.ReplyDataFetchResponseBody;
import org.roybond007.model.dto.ReviewDataFetchResponseBody;
import org.roybond007.model.helper.ReplyDataFetchObject;
import org.roybond007.model.helper.ReviewDataFetchObject;
import org.roybond007.repositories.MovieEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class MovieDataFetchServiceImpl implements MovieDataFetchService {

	@Value("${reply.list.pagesize}")
	private long replyPageSize;
	@Value("${review.list.pagesize}")
	private long reviewPageSize;
	
	
	private final MovieEntityRepository movieEntityRepository;
	
	@Autowired
	public MovieDataFetchServiceImpl(MovieEntityRepository movieEntityRepository) {
		this.movieEntityRepository = movieEntityRepository;
	}
	
	@Override
	public MovieInfoResponseBody fetchMovieInfo(String movieId, String userId) {
		
		Optional<MovieInfoResponseBody> target = null;
		
		
		try {
			target = movieEntityRepository.findMovieInfo(movieId, userId);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE,
					ErrorUtility.MOVIE_INFO_NOT_FOUND_MSG, movieId);
		}
		
		if(target.isEmpty()) {
			System.err.println("no movie found with id " + movieId);
			throw new ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND_CODE,
					ErrorUtility.MOVIE_INFO_NOT_FOUND_MSG, movieId);
		}
		
		
		MovieInfoResponseBody movieInfoResponseBody = target.get();

		String trailerLink = ServletUriComponentsBuilder
								.fromCurrentContextPath()
								.path("/" + movieInfoResponseBody.getTrailerLink())
								.toUriString();
		
		String posterLink = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/" + movieInfoResponseBody.getPosterLink())
				.toUriString();
		
		movieInfoResponseBody.setPosterLink(posterLink);
		movieInfoResponseBody.setTrailerLink(trailerLink);
		
		return movieInfoResponseBody;
	}


	@Override
	public ReplyDataFetchObject fetchSingleReply(String userId, String replyId) {
		
		ReplyDataFetchObject replyDataFetchObject = null;
		
		try {
			replyDataFetchObject = movieEntityRepository.findSingleReply(userId, replyId);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new 
				ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		
		if(replyDataFetchObject == null) {
			System.err.println("replyId " + replyId + " does not exist");
			throw new
				ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, replyId);
		}
		
		return replyDataFetchObject;
	}
	
	@Override
	public ReviewDataFetchObject fetchSingleReview(String userId, String reviewId) {
		
		ReviewDataFetchObject reviewDataFetchObject = null;
		
		try {
			reviewDataFetchObject = movieEntityRepository.findSingleReview(userId, reviewId, 0, (int)replyPageSize);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new
				ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(reviewDataFetchObject == null) {
			System.err.println("reviewId " + reviewId + " does not exist");
			throw new
				ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, reviewId);
		}
		
		reviewDataFetchObject.getReplyList().setId(0);
		reviewDataFetchObject.getReplyList().setSize(replyPageSize);
		reviewDataFetchObject.getReplyList().setLength(reviewDataFetchObject.getReplyList().getResult().size());
		
		return reviewDataFetchObject;
	}

	@Override
	public ReviewDataFetchResponseBody fetchReviewList(String userId, String movieId, long pageId) {
		
		ReviewDataFetchResponseBody reviewDataFetchResponseBody = null;
		
		try {
			reviewDataFetchResponseBody = movieEntityRepository
					.findReviewList(userId, movieId, (int)(pageId*reviewPageSize), (int)reviewPageSize, (int)replyPageSize);
			
			
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new
				ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(reviewDataFetchResponseBody == null) {
			System.err.println("movieId " + movieId + " does not exists");
			throw new
				ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, movieId);
		}
		
		reviewDataFetchResponseBody.setId(pageId);
		reviewDataFetchResponseBody.setSize(reviewPageSize);
		reviewDataFetchResponseBody.setLength(reviewDataFetchResponseBody.getResult().size());
		reviewDataFetchResponseBody.getResult().forEach(elem -> {
			elem.getReplyList().setId(0);
			elem.getReplyList().setSize(replyPageSize);
			elem.getReplyList().setLength(elem.getReplyList().getResult().size());
		});
		
		return reviewDataFetchResponseBody;
	}

	@Override
	public ReplyDataFetchResponseBody fetchReplyList(String userId, String reviewId, long pageId) {
		
		ReplyDataFetchResponseBody replyDataFetchResponseBody = null;
		
		try {
			replyDataFetchResponseBody = movieEntityRepository
					.findReplyList(userId, reviewId, (int)(pageId*replyPageSize), (int)replyPageSize);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new
				ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, null);
		}
		
		if(replyDataFetchResponseBody == null) {
			System.err.println("reviewId " + reviewId + " does not exists");
			throw new
				ContentLoadFailureException(ErrorUtility.ENTITY_NOT_FOUND_CODE, ErrorUtility.CONTENT_LOAD_FAILED_MSG, reviewId);
		}
		
		replyDataFetchResponseBody.setSize(replyPageSize);
		replyDataFetchResponseBody.setId(pageId);
		replyDataFetchResponseBody.setLength(replyDataFetchResponseBody.getResult().size());
		
		return replyDataFetchResponseBody;
	}

}

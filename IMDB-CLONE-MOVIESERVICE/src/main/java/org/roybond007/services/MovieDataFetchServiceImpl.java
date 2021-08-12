package org.roybond007.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.roybond007.exceptions.ContentLoadFailureException;
import org.roybond007.model.dto.MovieInfoResponseBody;
import org.roybond007.model.dto.MovieSearchResponseBody;
import org.roybond007.model.dto.MovieSearchResponseObject;
import org.roybond007.model.dto.ReplyDataFetchResponseBody;
import org.roybond007.model.dto.ReviewDataFetchResponseBody;
import org.roybond007.model.helper.ReplyDataFetchObject;
import org.roybond007.model.helper.ReviewDataFetchObject;
import org.roybond007.repositories.MovieEntityRepository;
import org.roybond007.repositories.ReplyEntityRepository;
import org.roybond007.repositories.ReviewEntityRepository;
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
	@Value("${moive.querylist.pagesize}")
	private long movieSearchPageSize;
	
	
	private final MovieEntityRepository movieEntityRepository;
	private final ReviewEntityRepository reviewEntityRepository;
	private final ReplyEntityRepository replyEntityRepository;
	
	
	@Autowired
	public MovieDataFetchServiceImpl(MovieEntityRepository movieEntityRepository,
			ReviewEntityRepository reviewEntityRepository,
			ReplyEntityRepository replyEntityRepository
	) {
		this.movieEntityRepository = movieEntityRepository;
		this.reviewEntityRepository = reviewEntityRepository;
		this.replyEntityRepository = replyEntityRepository;
	}
	
	
	@Override
	public MovieSearchResponseBody fetchTopMovies(long pageId, String userId) {
		List<MovieSearchResponseObject> responseBody = null;
		
		try {
			responseBody = 
					movieEntityRepository.findTopMovies((int)(pageId*movieSearchPageSize), (int)movieSearchPageSize, userId);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.MOVIE_INFO_NOT_FOUND_MSG, null);
		}
		
		MovieSearchResponseBody result = new MovieSearchResponseBody();
		result.setId(pageId);
		result.setSize(movieSearchPageSize);
		
		if(responseBody == null || responseBody.size() == 0) {
			result.setLength(0);
			result.setResult(new ArrayList<>());
		}else {
			result.setLength(responseBody.size());
			result.setResult(responseBody);
		}
		
		result.getResult().forEach(movie -> {
			String posterLink = ServletUriComponentsBuilder
									.fromCurrentContextPath()
									.path("/" + movie.getPosterLink())
									.toUriString();

			movie.setPosterLink(posterLink);
		});
		
		return result;
	}
	
	@Override
	public MovieSearchResponseBody fetchLeastMovies(long pageId, String userId) {
		List<MovieSearchResponseObject> responseBody = null;
		
		try {
			responseBody = 
					movieEntityRepository.findLeastMovies((int)(pageId*movieSearchPageSize), (int)movieSearchPageSize, userId);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.MOVIE_INFO_NOT_FOUND_MSG, null);
		}
		
		MovieSearchResponseBody result = new MovieSearchResponseBody();
		result.setId(pageId);
		result.setSize(movieSearchPageSize);
		
		if(responseBody == null || responseBody.size() == 0) {
			result.setLength(0);
			result.setResult(new ArrayList<>());
		}else {
			result.setLength(responseBody.size());
			result.setResult(responseBody);
		}
		
		result.getResult().forEach(movie -> {
			String posterLink = ServletUriComponentsBuilder
									.fromCurrentContextPath()
									.path("/" + movie.getPosterLink())
									.toUriString();

			movie.setPosterLink(posterLink);
		});
		
		return result;
	}
	
	@Override
	public MovieSearchResponseBody fetchRecentMovies(long pageId, String userId) {
		
		List<MovieSearchResponseObject> responseBody = null;
		
		try {
			responseBody = 
					movieEntityRepository.findRecentMovies((int)(pageId*movieSearchPageSize), (int)movieSearchPageSize, userId);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.MOVIE_INFO_NOT_FOUND_MSG, null);
		}
		
		MovieSearchResponseBody result = new MovieSearchResponseBody();
		result.setId(pageId);
		result.setSize(movieSearchPageSize);
		
		if(responseBody == null || responseBody.size() == 0) {
			result.setLength(0);
			result.setResult(new ArrayList<>());
		}else {
			result.setLength(responseBody.size());
			result.setResult(responseBody);
		}
		
		result.getResult().forEach(movie -> {
			String posterLink = ServletUriComponentsBuilder
									.fromCurrentContextPath()
									.path("/" + movie.getPosterLink())
									.toUriString();

			movie.setPosterLink(posterLink);
		});
		
		return result;
	}
	
	
	@Override
	public MovieSearchResponseBody fetchQueryMovies(String query, long pageId, String userId) {
		
		List<MovieSearchResponseObject> responseBody = null;
		
		String[] targetQuery = query.split(" ");
		
		String phases = "";
		
		for (String term : targetQuery) {
			phases = phases + "\"" + term + "\" ";
		}
		phases = phases.trim();
		
		System.out.println("phase to search = " + phases);
		
		try {
			responseBody = 
					movieEntityRepository.findQueryMovies(phases, (int)(pageId*movieSearchPageSize), (int)movieSearchPageSize, userId);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ContentLoadFailureException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.MOVIE_INFO_NOT_FOUND_MSG, null);
		}
		
		MovieSearchResponseBody result = new MovieSearchResponseBody();
		result.setId(pageId);
		result.setSize(movieSearchPageSize);
		
		if(responseBody == null || responseBody.size() == 0) {
			result.setLength(0);
			result.setResult(new ArrayList<>());
		}else {
			result.setLength(responseBody.size());
			result.setResult(responseBody);
		}
		
		result.getResult().forEach(movie -> {
			String posterLink = ServletUriComponentsBuilder
									.fromCurrentContextPath()
									.path("/" + movie.getPosterLink())
									.toUriString();

			movie.setPosterLink(posterLink);
		});
		
		return result;
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
			replyDataFetchObject = replyEntityRepository.findSingleReply(userId, replyId);
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
		
		String profileLink = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/" + replyDataFetchObject.getUserObject().getProfilePictureLink())
				.toUriString();

		replyDataFetchObject.getUserObject().setProfilePictureLink(profileLink);
		
		return replyDataFetchObject;
	}
	
	@Override
	public ReviewDataFetchObject fetchSingleReview(String userId, String reviewId) {
		
		ReviewDataFetchObject reviewDataFetchObject = null;
		
		try {
			reviewDataFetchObject = reviewEntityRepository.findSingleReview(userId, reviewId, 0, (int)replyPageSize);
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
		if(reviewDataFetchObject.getReplyList().getResult() == null)
			reviewDataFetchObject.getReplyList().setResult(new ArrayList<>());
		reviewDataFetchObject.getReplyList().setLength(reviewDataFetchObject.getReplyList().getResult().size());
		
		String profileLink = ServletUriComponentsBuilder
								.fromCurrentContextPath()
								.path("/" + reviewDataFetchObject.getUserObject().getProfilePictureLink())
								.toUriString();

		reviewDataFetchObject.getUserObject().setProfilePictureLink(profileLink);
		
		reviewDataFetchObject.getReplyList().getResult().forEach(reply -> {
			String link = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/" + reply.getUserObject().getProfilePictureLink())
					.toUriString();

			reply.getUserObject().setProfilePictureLink(link);
		});
		
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
		if(reviewDataFetchResponseBody.getResult() == null)
			reviewDataFetchResponseBody.setResult(new ArrayList<>());
		reviewDataFetchResponseBody.setLength(reviewDataFetchResponseBody.getResult().size());
		
		
		reviewDataFetchResponseBody.getResult().forEach(elem -> {
			
			String profileLink = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/" + elem.getUserObject().getProfilePictureLink())
					.toUriString();

			elem.getUserObject().setProfilePictureLink(profileLink);
			
			elem.getReplyList().setId(0);
			elem.getReplyList().setSize(replyPageSize);
			if(elem.getReplyList().getResult() == null)
				elem.getReplyList().setResult(new ArrayList<>());
			elem.getReplyList().setLength(elem.getReplyList().getResult().size());
			
			elem.getReplyList().getResult().forEach(reply -> {
				String link = ServletUriComponentsBuilder
						.fromCurrentContextPath()
						.path("/" + reply.getUserObject().getProfilePictureLink())
						.toUriString();

				reply.getUserObject().setProfilePictureLink(link);
			});
		});
		
		return reviewDataFetchResponseBody;
	}

	@Override
	public ReplyDataFetchResponseBody fetchReplyList(String userId, String reviewId, long pageId) {
		
		ReplyDataFetchResponseBody replyDataFetchResponseBody = null;
		
		try {
			replyDataFetchResponseBody = reviewEntityRepository
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
		if(replyDataFetchResponseBody.getResult() == null)
			replyDataFetchResponseBody.setResult(new ArrayList<>());
		replyDataFetchResponseBody.setLength(replyDataFetchResponseBody.getResult().size());
		
		replyDataFetchResponseBody.getResult().forEach(reply -> {
			String link = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/" + reply.getUserObject().getProfilePictureLink())
					.toUriString();

			reply.getUserObject().setProfilePictureLink(link);
		});
		
		return replyDataFetchResponseBody;
	}

}

package org.roybond007.services;

import org.roybond007.model.dto.MovieInfoResponseBody;
import org.roybond007.model.dto.MovieSearchResponseBody;
import org.roybond007.model.dto.ReplyDataFetchResponseBody;
import org.roybond007.model.dto.ReviewDataFetchResponseBody;
import org.roybond007.model.helper.ReplyDataFetchObject;
import org.roybond007.model.helper.ReviewDataFetchObject;

public interface MovieDataFetchService {

	MovieInfoResponseBody fetchMovieInfo(String movieId, String userId);

	ReplyDataFetchObject fetchSingleReply(String userId, String replyId);

	ReviewDataFetchObject fetchSingleReview(String userId, String reviewId);

	ReviewDataFetchResponseBody fetchReviewList(String userId, String movieId, long pageId);

	ReplyDataFetchResponseBody fetchReplyList(String userId, String reviewId, long pageId);

	MovieSearchResponseBody fetchQueryMovies(String query, long pageId, String userId);

	MovieSearchResponseBody fetchRecentMovies(long pageId, String userId);

	MovieSearchResponseBody fetchTopMovies(long pageId, String userId);

	MovieSearchResponseBody fetchLeastMovies(long pageId, String userId);

	
}

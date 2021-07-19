package org.roybond007.services;

import org.roybond007.model.dto.MovieInfoResponseBody;

public interface MovieDataFetchService {

	MovieInfoResponseBody fetchMovieInfo(String movieId, String userId);

}

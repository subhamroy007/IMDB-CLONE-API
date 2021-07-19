package org.roybond007.services;

import java.util.Optional;

import org.roybond007.exceptions.MovieEntityNotFoundException;
import org.roybond007.model.dto.MovieInfoResponseBody;
import org.roybond007.repositories.MovieEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class MovieDataFetchServiceImpl implements MovieDataFetchService {

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
			throw new MovieEntityNotFoundException(ErrorUtility.DATA_LAYER_ERROR_CODE,
					ErrorUtility.MOVIE_INFO_NOT_FOUND_MSG, movieId);
		}
		
		if(target.isEmpty()) {
			System.err.println("no movie found with id " + movieId);
			throw new MovieEntityNotFoundException(ErrorUtility.ENTITY_NOT_FOUND_CODE,
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

}

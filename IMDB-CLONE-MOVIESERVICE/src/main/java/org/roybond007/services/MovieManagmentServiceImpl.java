package org.roybond007.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.roybond007.exceptions.MovieUploadFailedException;
import org.roybond007.model.dto.MovieUploadRequestBody;
import org.roybond007.model.dto.MovieUploadResponseBody;
import org.roybond007.model.entity.MovieEntity;
import org.roybond007.model.helper.RatingReference;
import org.roybond007.repositories.MovieEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
public class MovieManagmentServiceImpl implements MovieManagmentService {

	private final MovieEntityRepository movieEntityRepository;

	@Autowired
	public MovieManagmentServiceImpl(MovieEntityRepository movieEntityRepository) {
		this.movieEntityRepository = movieEntityRepository;
	}

	@Override
	public MovieUploadResponseBody uploadMovie(MovieUploadRequestBody movieUploadRequestBody) {

		MovieEntity movieEntity = populateMovieEntity(movieUploadRequestBody);

		MovieEntity result = null;

		try {
			result = movieEntityRepository.save(movieEntity);
		} catch (DataAccessException e) {
			
			System.err.println(e.getLocalizedMessage());
			throw new MovieUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG);
		}
		
		return new MovieUploadResponseBody(result.getId(), result.getTitle(), result.getDescription(),
				result.getTimestamp(), result.getLength());
	}

	private MovieEntity populateMovieEntity(MovieUploadRequestBody movieUploadRequestBody) {

		MovieEntity movieEntity = new MovieEntity();
		movieEntity.setAvgRating(0);
		movieEntity.setDescription(movieUploadRequestBody.getDescription());
		movieEntity.setGenres(movieUploadRequestBody.getGenres());
		movieEntity.setId("Movie@" + System.currentTimeMillis());
		movieEntity.setLength(movieUploadRequestBody.getLength());
		movieEntity.setNoOfRatings(0);
		movieEntity.setNoOfReviews(0);
		movieEntity.setTimestamp(System.currentTimeMillis());
		movieEntity.setTitle(movieUploadRequestBody.getTitle());
		movieEntity.setRatingList(new ArrayList<RatingReference>());
		movieEntity.setReviewList(new ArrayList<String>());
		movieEntity.setPosterLink(savePoster(movieUploadRequestBody.getPoster()));
		movieEntity.setTrailerLink(saveTrailer(movieUploadRequestBody.getTrailer()));

		return movieEntity;
	}

	private String saveTrailer(MultipartFile trailer) {

		String trailerName = "trailer@" + System.currentTimeMillis() + trailer.getOriginalFilename();

		FileSystemResource fileSystemResource = new FileSystemResource("C:" + File.separator + "trailers");

		if (!fileSystemResource.exists()) {
			try {
				Files.createDirectory(Paths.get(fileSystemResource.getURI()));
			} catch (IOException e) {

				System.err.println(e.getLocalizedMessage());
				throw new MovieUploadFailedException(ErrorUtility.FILE_SYSTEM_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG);

			}
		}

		Path savePath = null;

		try {
			savePath = Paths.get(fileSystemResource.getURI()).resolve(trailerName);
			Files.copy(trailer.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

			System.err.println(e.getLocalizedMessage());
			throw new MovieUploadFailedException(ErrorUtility.FILE_SYSTEM_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG);
		}

		
		return "trailers/" + trailerName;
	}

	private String savePoster(MultipartFile poster) {

		String posterName = "poster@" + System.currentTimeMillis() + poster.getOriginalFilename();

		FileSystemResource fileSystemResource = new FileSystemResource("C:" + File.separator + "posters");

		if (!fileSystemResource.exists()) {
			try {
				Files.createDirectory(Paths.get(fileSystemResource.getURI()));
			} catch (IOException e) {

				System.err.println(e.getLocalizedMessage());
				throw new MovieUploadFailedException(ErrorUtility.FILE_SYSTEM_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG);

			}
		}

		Path savePath = null;

		try {
			savePath = Paths.get(fileSystemResource.getURI()).resolve(posterName);
			Files.copy(poster.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

			System.err.println(e.getLocalizedMessage());
			throw new MovieUploadFailedException(ErrorUtility.FILE_SYSTEM_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG);
		}

		return "posters/" + posterName;
	}

}

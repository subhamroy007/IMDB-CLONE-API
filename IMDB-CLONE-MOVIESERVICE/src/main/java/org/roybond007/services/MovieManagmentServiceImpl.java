package org.roybond007.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.roybond007.exceptions.MovieUploadFailedException;
import org.roybond007.exceptions.ReviewUploadFailedException;
import org.roybond007.model.dto.MovieUploadRequestBody;
import org.roybond007.model.dto.MovieUploadResponseBody;
import org.roybond007.model.dto.RatingUploadRequestBody;
import org.roybond007.model.dto.RatingUploadResponseBody;
import org.roybond007.model.dto.ReactUploadResponseBody;
import org.roybond007.model.dto.ReplyUploadResponseBody;
import org.roybond007.model.dto.ReviewUploadRequestBody;
import org.roybond007.model.dto.ReviewUploadResponseBody;
import org.roybond007.model.entity.MovieEntity;
import org.roybond007.model.entity.ReplyEntity;
import org.roybond007.model.entity.ReviewEntity;
import org.roybond007.repositories.MovieEntityRepository;
import org.roybond007.repositories.ReplyEntityRepository;
import org.roybond007.repositories.ReviewEntityRepository;
import org.roybond007.utils.ErrorUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class MovieManagmentServiceImpl implements MovieManagmentService {

	private final MovieEntityRepository movieEntityRepository;

	private final ReviewEntityRepository reviewEntityRepository;

	private final ReplyEntityRepository replyEntityRepository;

	@Autowired
	public MovieManagmentServiceImpl(MovieEntityRepository movieEntityRepository,
		ReviewEntityRepository reviewEntityRepository, ReplyEntityRepository replyEntityRepository) {
		this.movieEntityRepository = movieEntityRepository;
		this.reviewEntityRepository = reviewEntityRepository;
		this.replyEntityRepository = replyEntityRepository;
	}

	@Override
	public MovieUploadResponseBody uploadMovie(MovieUploadRequestBody movieUploadRequestBody) {

		MovieEntity movieEntity = populateMovieEntity(movieUploadRequestBody);

		MovieEntity result = null;

		try {
			result = movieEntityRepository.save(movieEntity);
		} catch (DataAccessException e) {
			
			System.err.println(e.getLocalizedMessage());
			throw new 
				MovieUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG, null);
		}
		
		return new MovieUploadResponseBody(result.getId(), result.getTitle(), result.getDescription(),
				result.getTimestamp(), result.getLength());
	}

	private MovieEntity populateMovieEntity(MovieUploadRequestBody movieUploadRequestBody) {

		MovieEntity movieEntity = new MovieEntity();
		movieEntity.setTotalRating(0);
		movieEntity.setDescription(movieUploadRequestBody.getDescription());
		movieEntity.setGenres(movieUploadRequestBody.getGenres());
		movieEntity.setId("Movie@" + System.currentTimeMillis());
		movieEntity.setLength(movieUploadRequestBody.getLength());
		movieEntity.setNoOfRatings(0);
		movieEntity.setNoOfReviews(0);
		movieEntity.setTimestamp(System.currentTimeMillis());
		movieEntity.setTitle(movieUploadRequestBody.getTitle());
		movieEntity.setRatingList(new ArrayList<>());
		movieEntity.setReviewList(new ArrayList<>());
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
				throw new 
					MovieUploadFailedException(ErrorUtility.FILE_SYSTEM_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG, null);

			}
		}

		Path savePath = null;

		try {
			savePath = Paths.get(fileSystemResource.getURI()).resolve(trailerName);
			Files.copy(trailer.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

			System.err.println(e.getLocalizedMessage());
			throw new 
				MovieUploadFailedException(ErrorUtility.FILE_SYSTEM_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG, null);
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
				throw new 
					MovieUploadFailedException(ErrorUtility.FILE_SYSTEM_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG, null);

			}
		}

		Path savePath = null;

		try {
			savePath = Paths.get(fileSystemResource.getURI()).resolve(posterName);
			Files.copy(poster.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

			System.err.println(e.getLocalizedMessage());
			throw new 
				MovieUploadFailedException(ErrorUtility.FILE_SYSTEM_ERROR_CODE, ErrorUtility.MOVIE_UPLOAD_FAILED_MSG, null);
		}

		return "posters/" + posterName;
	}

	@Override
	public ReviewUploadResponseBody uploadReview(String userId, String movieId,
			ReviewUploadRequestBody reviewUploadRequestBody) {
		
		ReviewEntity reviewEntity = new ReviewEntity();
		reviewEntity.setContent(reviewUploadRequestBody.getContent());
		reviewEntity.setId("Review@" + System.currentTimeMillis());
		reviewEntity.setLikeList(new ArrayList<>());
		reviewEntity.setMovieId(movieId);
		reviewEntity.setReplyList(new ArrayList<>());
		reviewEntity.setTimestamp(System.currentTimeMillis());
		reviewEntity.setUserId(userId);
		
		ReviewEntity target = null;
		
		try {
			target = reviewEntityRepository.save(reviewEntity);			
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ReviewUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE,
				ErrorUtility.CONTENT_UPLOAD_FAILED_MSG, null);
		}
		
		reviewEntityRepository.uploadReviewToUser(target);
		reviewEntityRepository.uploadReviewToMovie(target);

		return new ReviewUploadResponseBody(userId, movieId, target.getContent(), target.getTimestamp());
	}

	@Override
	public ReplyUploadResponseBody uploadReply(String userId, String reviewId,
			ReviewUploadRequestBody reviewUploadRequestBody) {
		
		ReplyEntity replyEntity = new ReplyEntity();
		replyEntity.setContent(reviewUploadRequestBody.getContent());
		replyEntity.setId("Reply@" + System.currentTimeMillis());
		replyEntity.setLikeList(new ArrayList<>());
		replyEntity.setReviewId(reviewId);
		replyEntity.setTimestamp(System.currentTimeMillis());
		replyEntity.setUserId(userId);
		
		ReplyEntity target = null;

		try {
			target = replyEntityRepository.save(replyEntity);
		} catch (DataAccessException e) {
			System.err.println(e.getLocalizedMessage());
			throw new ReviewUploadFailedException(ErrorUtility.DATA_LAYER_ERROR_CODE, 
				ErrorUtility.CONTENT_UPLOAD_FAILED_MSG, null);
		}

		replyEntityRepository.uploadReplyToReview(target);

		return new ReplyUploadResponseBody(userId, reviewId, target.getContent(), target.getTimestamp());
	}

	@Override
	public ReactUploadResponseBody updateReviewReact(String userId, String reviewId) {
		ReactUploadResponseBody reactUploadResponseBody = reviewEntityRepository.uploadReacToReview(userId, reviewId);
		return reactUploadResponseBody;
	}

	@Override
	public ReactUploadResponseBody updateReplyReact(String userId, String replyId) {
		ReactUploadResponseBody reactUploadResponseBody = replyEntityRepository.uploadReacToReply(userId, replyId);
		return reactUploadResponseBody;
	}

	@Override
	public RatingUploadResponseBody uploadRating(RatingUploadRequestBody ratingUploadRequestBody, String userId,
			String movieId) {
		
		RatingUploadResponseBody ratingUploadResponseBody = movieEntityRepository
				.uploadRatingToMovie(ratingUploadRequestBody, userId, movieId);
		
		return ratingUploadResponseBody;
	}
	
}

package org.roybond007.repositories;

import org.roybond007.model.dto.RatingUploadRequestBody;
import org.roybond007.model.dto.RatingUploadResponseBody;

public interface MovieEntityRepositoryCustom {

	RatingUploadResponseBody uploadRatingToMovie(RatingUploadRequestBody ratingUploadRequestBody, String userId,
			String movieId);

}

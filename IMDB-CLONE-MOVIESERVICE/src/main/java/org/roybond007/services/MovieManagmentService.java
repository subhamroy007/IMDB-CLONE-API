package org.roybond007.services;




import org.roybond007.model.dto.MovieUploadRequestBody;
import org.roybond007.model.dto.MovieUploadResponseBody;
import org.roybond007.model.dto.ReviewUploadRequestBody;
import org.roybond007.model.dto.ReviewUploadResponseBody;

public interface MovieManagmentService {

	MovieUploadResponseBody uploadMovie(MovieUploadRequestBody movieUploadRequestBody);

    ReviewUploadResponseBody uploadReview(String userId, String movieId, 
		ReviewUploadRequestBody reviewUploadRequestBody);

}

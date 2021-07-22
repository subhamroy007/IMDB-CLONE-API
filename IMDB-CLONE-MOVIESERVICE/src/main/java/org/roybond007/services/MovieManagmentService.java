package org.roybond007.services;





import org.roybond007.model.dto.MovieUploadRequestBody;
import org.roybond007.model.dto.MovieUploadResponseBody;
import org.roybond007.model.dto.ReactUploadResponseBody;
import org.roybond007.model.dto.ReplyUploadResponseBody;
import org.roybond007.model.dto.ReviewUploadRequestBody;
import org.roybond007.model.dto.ReviewUploadResponseBody;

public interface MovieManagmentService {

	MovieUploadResponseBody uploadMovie(MovieUploadRequestBody movieUploadRequestBody);

    ReviewUploadResponseBody uploadReview(String userId, String movieId, 
		ReviewUploadRequestBody reviewUploadRequestBody);

    ReplyUploadResponseBody uploadReply(String userId, String reviewId,
    	ReviewUploadRequestBody reviewUploadRequestBody);

    ReactUploadResponseBody updateReviewReact(String userid, String reviewId);

    ReactUploadResponseBody updateReplyReact(String userId, String replyId);

}

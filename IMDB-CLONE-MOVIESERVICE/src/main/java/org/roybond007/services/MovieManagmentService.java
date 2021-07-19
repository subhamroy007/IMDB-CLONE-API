package org.roybond007.services;



import org.roybond007.model.dto.MovieUploadRequestBody;
import org.roybond007.model.dto.MovieUploadResponseBody;

public interface MovieManagmentService {

	MovieUploadResponseBody uploadMovie(MovieUploadRequestBody movieUploadRequestBody);

}

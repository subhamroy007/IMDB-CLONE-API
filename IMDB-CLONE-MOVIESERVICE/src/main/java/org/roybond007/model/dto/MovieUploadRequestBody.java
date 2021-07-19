package org.roybond007.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.roybond007.utils.CustomPosterConstraint;
import org.roybond007.utils.CustomTrailerConstraint;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MovieUploadRequestBody {

	@NotNull(message = "title cannot be null")
	@Size(min = 5, max = 200, message = "must be 5 to 100 charecter long")
	private String title;
	
	@NotNull(message = "description cannot be null")
	@Size(min = 10, max = 2000, message = "must be 10 to 1000 charecter long")
	private String description;
	
	@NotNull(message = "length cannot be null")
	@Max(value = 180, message = "cannot be more then 180 miniutes")
	@Min(value = 30, message = "cannot be less then 30 miniutes")
	private long length;
	
	@NotNull(message = "genre cannot be null")
	private String genres;
	
	@NotNull(message = "must include a trailer video")
	@CustomTrailerConstraint
	private MultipartFile trailer;
	
	@NotNull(message = "must include a poster image")
	@CustomPosterConstraint
	private MultipartFile poster;
	
}

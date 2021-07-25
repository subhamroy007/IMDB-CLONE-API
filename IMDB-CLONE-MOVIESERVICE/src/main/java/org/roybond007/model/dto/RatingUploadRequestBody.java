package org.roybond007.model.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RatingUploadRequestBody {

	@NotNull(message = "rating value is required")
	@Max(value = 5, message = "rating value should not be more then 5")
	@Min(value = 1, message = "rating value should not be less then 1")
	private long rating;
	
}

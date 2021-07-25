package org.roybond007.model.dto;

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
public class MovieInfoResponseBody {

	private String id;
	private String title;
	private String description;
	private long timestamp;
	private long length;
	private String posterLink;
	private String trailerLink;
	private long noOfReviews;
	private long noOfRatings;
	private double avgRating;
	private String genres;
	private long userRating;
	
	
}

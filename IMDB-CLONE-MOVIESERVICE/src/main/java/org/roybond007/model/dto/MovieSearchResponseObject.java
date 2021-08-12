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
public class MovieSearchResponseObject {

	private String id;
	private String title;
	private long length;
	private String posterLink;
	private long noOfRatings;
	private double avgRating;
	private String genres;
	private long userRating;
	private boolean isWishListed;
	private boolean isWatchListed;
	
}

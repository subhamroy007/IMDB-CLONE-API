package org.roybond007.model.helper;

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
public class EmbeddedMovieObject {
	
	private String id;
	private String title;
	private String description;
	private String genres;
	private String posterLink;
	private double avgRating;
	private long noOfRatings;
	private long userRating;
	
}

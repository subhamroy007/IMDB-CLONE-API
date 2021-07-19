package org.roybond007.model.entity;

import java.util.List;

import org.roybond007.model.helper.RatingReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(value = "movieEntity")
@TypeAlias(value = "movieEntity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MovieEntity {

	@Id
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
	private List<RatingReference> ratingList;
	private List<String> reviewList;

	@Version
	private long version;
	
}

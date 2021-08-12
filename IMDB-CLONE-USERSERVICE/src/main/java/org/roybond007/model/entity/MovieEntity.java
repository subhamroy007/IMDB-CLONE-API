package org.roybond007.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.roybond007.model.helper.EntityReferenceWithTimestamp;
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
	private long totalRating;
	private long wishListLength;
	private long watchListLength;
	private String genres;
	private List<RatingReference> ratingList;
	private List<EntityReferenceWithTimestamp> reviewList;
	private List<EntityReferenceWithTimestamp> wishList = new ArrayList<>();
	private List<EntityReferenceWithTimestamp> watchList = new ArrayList<>();

	@Version
	private long version;
	
}

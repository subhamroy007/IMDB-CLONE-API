package org.roybond007.model.entity;

import java.util.ArrayList;
import java.util.List;

import org.roybond007.model.helper.RatingReference;
import org.roybond007.model.helper.EntityReferenceWithTimestamp;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TypeAlias("userEntity")
@Document("userEntity")
public class UserEntity {

	@Id
	private String id;
	
	private String userId;
	private String password;
	private String[] roles;
	private boolean isActive;
	private String emailId;
	private String profilePictureLink;
	private long noOfFollowers;
	private long noOfFollowings;
	private long noOfMovieReviewed;
	private long noOfMovieRated;
	private long wishListLength;
	private long watchListLength;
	
	private List<EntityReferenceWithTimestamp> followerList = new ArrayList<>();
	private List<EntityReferenceWithTimestamp> followingList = new ArrayList<>();
	private List<RatingReference> ratingList = new ArrayList<>();
	private List<EntityReferenceWithTimestamp> reviewList = new ArrayList<>();
	private List<EntityReferenceWithTimestamp> wishList = new ArrayList<>();
	private List<EntityReferenceWithTimestamp> watchList = new ArrayList<>();
	
	
	@Version
	private long version;
	
		
}

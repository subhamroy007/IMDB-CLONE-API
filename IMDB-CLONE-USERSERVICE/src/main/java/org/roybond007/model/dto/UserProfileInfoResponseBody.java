package org.roybond007.model.dto;


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
public class UserProfileInfoResponseBody {
	
	private String id;
	private String userId;
	private boolean isAdmin;
	private String profilePictureLink;
	private long noOfFollowers;
	private long noOfFollowings;
	private long noOfMovieReviewed;
	private long noOfMovieRated;
	private long wishListLength;
	private long watchListLength;
	private boolean isFollowing;
	private boolean isEditable;
	
	private MoviePageObject ratingList;
	private MoviePageObject wishList;
	private MoviePageObject watchList;
}

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
public class HomePageResponseBody {

	private UserInfoResponseBody userInfo;
	
	private MovieSearchResponseBody recentlyAdded;
	
	private MovieSearchResponseBody topRated;
	
	private MovieSearchResponseBody leastRated;
	
}

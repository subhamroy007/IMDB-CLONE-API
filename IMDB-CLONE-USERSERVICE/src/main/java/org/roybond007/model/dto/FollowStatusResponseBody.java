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
public class FollowStatusResponseBody {

	private String followerId;
	private String followingId;
	private long noOfFollowers;
	private boolean status;
	
	
}

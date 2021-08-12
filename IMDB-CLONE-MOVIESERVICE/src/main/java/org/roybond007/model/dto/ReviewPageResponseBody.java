package org.roybond007.model.dto;


import org.roybond007.model.helper.ReviewDataFetchObject;

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
public class ReviewPageResponseBody {

	UserInfoResponseBody userInfo;
	
	ReviewDataFetchObject review;
}

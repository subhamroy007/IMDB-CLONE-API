package org.roybond007.model.helper;

import java.util.List;

import org.roybond007.model.dto.ReplyDataFetchResponseBody;

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
public class ReviewDataFetchObject {

	private String id;
	private long timestamp;
	private String userId;
	private EntityReferenceWithPicture userObject;
	private long noOfLikes;
	private long noOfReplies;
	private boolean userReact;
	
	private ReplyDataFetchResponseBody replyList;
	
}

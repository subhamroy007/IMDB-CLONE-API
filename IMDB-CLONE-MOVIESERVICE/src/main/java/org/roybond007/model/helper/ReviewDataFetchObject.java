package org.roybond007.model.helper;

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
	private String content;
	private EntityReferenceWithPicture userObject;
	private long noOfLikes;
	private long noOfReplies;
	private boolean userReact;
	ReplyDataFetchResponseBody replyList;
	
}

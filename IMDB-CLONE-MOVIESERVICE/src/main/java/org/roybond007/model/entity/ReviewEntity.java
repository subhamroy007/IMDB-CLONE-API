package org.roybond007.model.entity;

import java.util.List;

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

@Document(value = "reviewEntity")
@TypeAlias(value = "reviewEntity")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ReviewEntity {

	@Id
	private String id;
	private long timestamp;
	private String userId;
	private String movieId;
	private String content;
	private long noOfLikes;
	private long noOfReplies;
	private List<EntityReferenceWithTimestamp> likeList;
	private List<String> replyList;
	
	@Version
	private long version;
}

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

@Document(value = "replyEntity")
@TypeAlias(value = "replyEntity")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ReplyEntity {

	@Id
	private String id;
	private long timestamp;
	private String content;
	private String userId;
	private String reviewId;
	private long noOfLikes;
	private List<EntityReferenceWithTimestamp> likeList;
	
	@Version
	private long version;
}

package org.roybond007.model.helper;

import org.springframework.data.annotation.TypeAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@TypeAlias("ratingReference")
public class RatingReference {

	private String id;
	
	private long rating;
	
	private long timestamp;
	
	
	
}

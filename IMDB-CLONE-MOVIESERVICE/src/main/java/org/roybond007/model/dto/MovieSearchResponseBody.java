package org.roybond007.model.dto;

import java.util.List;

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
public class MovieSearchResponseBody {

	private long id;
	private long size;
	private long length;
	
	List<MovieSearchResponseObject> result;
	
}

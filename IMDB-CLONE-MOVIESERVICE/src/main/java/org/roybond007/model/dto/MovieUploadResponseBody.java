package org.roybond007.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MovieUploadResponseBody {

	private String id;
	private String title;
	private String description;
	private long timestamp;
	private long length;
	
	
}


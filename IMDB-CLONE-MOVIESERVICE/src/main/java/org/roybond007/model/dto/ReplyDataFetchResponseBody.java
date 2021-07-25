package org.roybond007.model.dto;

import java.util.List;

import org.roybond007.model.helper.ReplyDataFetchObject;

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
public class ReplyDataFetchResponseBody {

	private long id;
	private long size;
	private long length;
	
	private List<ReplyDataFetchObject> result;
}

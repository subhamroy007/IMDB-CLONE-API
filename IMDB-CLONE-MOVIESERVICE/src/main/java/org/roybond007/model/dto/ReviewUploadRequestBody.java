package org.roybond007.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class ReviewUploadRequestBody {
    
    @NotNull(message = "content cannot be null")
    @Size(min = 1, message = "content cannot be empty")
    private String content;

}

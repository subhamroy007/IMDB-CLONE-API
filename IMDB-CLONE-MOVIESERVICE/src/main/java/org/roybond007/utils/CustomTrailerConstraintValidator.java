package org.roybond007.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class CustomTrailerConstraintValidator implements 
ConstraintValidator<CustomTrailerConstraint, MultipartFile>{

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		
		if((value == null) || value.isEmpty())
			return false;
		if(value.getSize() > (200 * 1024 * 1024) || value.getSize() < (5 * 1024 * 1024))
			return false;
		if(!value.getContentType().startsWith("video"))
			return false;
		
		
		return true;
	}

}

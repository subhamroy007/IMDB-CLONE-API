package org.roybond007.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class CustomPosterConstraintValidator implements 
ConstraintValidator<CustomPosterConstraint, MultipartFile>{

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		if((value == null) || value.isEmpty())
			return false;
		if(value.getSize() > (5 * 1024 * 1024) || value.getSize() < (50 * 1024))
			return false;
		if(!value.getContentType().startsWith("image"))
			return false;
		
		
		return true;
	}

	
	
}

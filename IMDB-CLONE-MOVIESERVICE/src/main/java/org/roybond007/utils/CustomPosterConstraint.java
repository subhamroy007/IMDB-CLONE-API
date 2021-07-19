package org.roybond007.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = CustomPosterConstraintValidator.class)  
@Target( { ElementType.METHOD, ElementType.FIELD } )  
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPosterConstraint {
	
	public String message() default "must be a valid image file";
	public Class<?>[] groups() default {};  
	public Class<? extends Payload>[] payload() default {}; 
	
}

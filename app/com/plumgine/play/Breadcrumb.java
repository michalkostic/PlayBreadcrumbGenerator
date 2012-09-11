package com.plumgine.play;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Breadcrumb {
	public String value();	
	public int level() default 0;
	public boolean ignoreNext() default false;
	public boolean replacePreviousAtLevel() default false;
}
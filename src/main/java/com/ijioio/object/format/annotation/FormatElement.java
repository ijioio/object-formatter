package com.ijioio.object.format.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ijioio.object.format.extractor.Extractor;
import com.ijioio.object.format.formatter.Formatter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormatElement {

	public String value() default "";

	public Class<? extends Extractor<?>> extractor() default Extractor.Default.class;

	public Class<? extends Formatter<?>> formatter() default Formatter.Default.class;
}
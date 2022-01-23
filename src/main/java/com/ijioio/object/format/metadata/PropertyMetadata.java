package com.ijioio.object.format.metadata;

import java.util.Set;

import com.ijioio.object.format.extractor.Extractor;
import com.ijioio.object.format.formatter.Formatter;

public interface PropertyMetadata {

	public static final String THIS = "this";

	public String getId();

	public Class<?> getType();

	public Class<? extends Extractor<?>> getExtractor();

	public Class<? extends Formatter<?>> getFormatter();

	public Set<String> getAliases();

	public Object getValue(Object owner) throws Exception;
}

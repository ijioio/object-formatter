package com.ijioio.object.format.metadata;

import java.util.List;
import java.util.Set;

import com.ijioio.object.format.formatter.Formatter;

public interface ObjectMetadata {

	public Class<?> getType();

	public String getId();

	public Class<? extends Formatter<?>> getFormatter();

	public Set<String> getAliases();

	public List<PropertyMetadata> getProperties();
}

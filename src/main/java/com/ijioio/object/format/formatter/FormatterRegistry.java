package com.ijioio.object.format.formatter;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormatterRegistry {

	public static FormatterRegistry get() {
		return registry;
	}

	private static final FormatterRegistry registry = new FormatterRegistry();

	private final Map<Class<?>, Formatter<?>> formatters = new HashMap<>();

	private FormatterRegistry() {

		register(Boolean.class, new BooleanFormatter());
		register(Character.class, new CharacterFormatter());
		register(String.class, new StringFormatter());
		register(Number.class, new NumberFormatter());
		register(Date.class, new DateFormatter());
		register(Collection.class, new CollectionFormatter());
	}

	public void register(Class<?> type, Formatter<?> formatter) {
		formatters.put(type, formatter);
	}

	public Formatter<?> getFormatter(Class<?> type) {
		return formatters.get(type);
	}
}

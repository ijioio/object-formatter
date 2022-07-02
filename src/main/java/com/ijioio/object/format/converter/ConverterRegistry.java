package com.ijioio.object.format.converter;

import java.util.HashMap;
import java.util.Map;

public class ConverterRegistry {

	public static ConverterRegistry get() {
		return registry;
	}

	private static final ConverterRegistry registry = new ConverterRegistry();

	private final Map<Class<?>, Converter<?, ?>> converters = new HashMap<>();

	private ConverterRegistry() {
		// Empty
	}

	public void register(Class<?> type, Converter<?, ?> converter) {
		converters.put(type, converter);
	}

	public Converter<?, ?> getConverter(Class<?> type) {
		return converters.get(type);
	}
}

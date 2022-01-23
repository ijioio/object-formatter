package com.ijioio.object.format.formatter;

import java.util.Locale;
import java.util.Optional;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.object.ObjectHolder;

public class BooleanFormatter implements Formatter<Boolean> {

	@Override
	public String format(ObjectHolder<Boolean> objectHolder, Configuration configuration, String separator, String pattern,
			Locale locale) throws Exception {

		Boolean value = objectHolder.getObject();

		return Optional.ofNullable(value).map(item -> String.valueOf(item)).orElse(null);
	}
}

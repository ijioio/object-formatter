package com.ijioio.object.format.formatter;

import java.util.Locale;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.object.ObjectHolder;

public class StringFormatter implements Formatter<String> {

	@Override
	public String format(ObjectHolder<String> objectHolder, Configuration configuration, String separator, String pattern,
			Locale locale) throws Exception {

		String value = objectHolder.getObject();

		return value;
	}
}

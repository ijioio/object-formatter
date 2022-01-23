package com.ijioio.object.format.formatter;

import java.util.Locale;
import java.util.Optional;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.object.ObjectHolder;

public class CharacterFormatter implements Formatter<Character> {

	@Override
	public String format(ObjectHolder<Character> objectHolder, Configuration configuration, String separator, String pattern,
			Locale locale) throws Exception {

		Character value = objectHolder.getObject();

		return Optional.ofNullable(value).map(item -> String.valueOf(item)).orElse(null);
	}
}

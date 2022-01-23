package com.ijioio.object.format.formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.object.ObjectHolder;

public class LocalDateFormatter implements Formatter<LocalDate> {

	@Override
	public String format(ObjectHolder<LocalDate> objectHolder, Configuration configuration, String separator,
			String pattern, Locale locale) throws Exception {

		LocalDate value = objectHolder.getObject();

		return Optional.ofNullable(value)
				.map(item -> pattern != null ? DateTimeFormatter.ofPattern(pattern, locale).format(item)
						: DateTimeFormatter.ISO_LOCAL_DATE.format(item))
				.orElse(null);
	}
}

package com.ijioio.object.format.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.object.ObjectHolder;

public class DateFormatter implements Formatter<Date> {

	@Override
	public String format(ObjectHolder<Date> objectHolder, Configuration configuration, String separator, String pattern,
			Locale locale) throws Exception {

		Date value = objectHolder.getObject();

		return Optional.ofNullable(value).map(
				item -> pattern != null ? new SimpleDateFormat(pattern, locale).format(item) : String.valueOf(item))
				.orElse(null);
	}
}

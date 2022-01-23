package com.ijioio.object.format.formatter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.object.ObjectHolder;

public class NumberFormatter implements Formatter<Number> {

	@Override
	public String format(ObjectHolder<Number> objectHolder, Configuration configuration, String separator, String pattern,
			Locale locale) throws Exception {

		Number value = objectHolder.getObject();

		return Optional.ofNullable(value).map(item -> {

			if (pattern != null) {

				DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(locale);

				format.applyPattern(pattern);

				return format.format(value);
			}

			return String.valueOf(item);

		}).orElse(null);
	}
}

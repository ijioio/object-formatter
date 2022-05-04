package com.ijioio.object.format.formatter;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.ObjectFormatter;
import com.ijioio.object.format.object.ObjectHolder;
import com.ijioio.object.format.util.PatternUtil.Pattern;

public class CollectionFormatter implements Formatter<Collection<?>> {

	@Override
	public String format(ObjectHolder<Collection<?>> objectHolder, Configuration configuration, String separator,
			String pattern, Locale locale) throws Exception {

		Collection<?> values = objectHolder.getObject();

		return Optional.ofNullable(values).map(items -> {

			ObjectFormatter format = ObjectFormatter.of(pattern != null ? pattern : Pattern.self());

			return items.stream().map(item -> ObjectHolder.of(item, objectHolder))
					.map(item -> format.format(item, locale))
					.collect(Collectors.joining(separator != null ? separator : Pattern.SEPARATOR));

		}).orElse(null);
	}
}

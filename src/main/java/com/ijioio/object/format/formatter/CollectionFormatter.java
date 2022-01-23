package com.ijioio.object.format.formatter;

import java.util.Collection;
import java.util.Locale;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.object.ObjectHolder;

public class CollectionFormatter implements Formatter<Collection<?>> {

	@Override
	public String format(ObjectHolder<Collection<?>> objectHolder, Configuration configuration, String separator,
			String pattern, Locale locale) throws Exception {

		Collection<?> values = objectHolder.getObject();

//		return Optional.ofNullable(values).map(items -> {
//
//			ObjectFormatter format = ObjectFormatter.of(pattern != null ? pattern : Pattern.self());
//
//			return items.stream().map(item -> FormatObject.of(item, object)).map(item -> format.format(item, locale))
//					.collect(Collectors.joining(separator != null ? separator : Pattern.SEPARATOR));
//
//		}).orElse(null);

		return null;
	}
}

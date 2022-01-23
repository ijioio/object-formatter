package com.ijioio.object.format.formatter;

import java.util.Locale;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.object.ObjectHolder;

public interface Formatter<V> {

	public String format(ObjectHolder<V> objectHolder, Configuration configuration, String separator, String pattern,
			Locale locale) throws Exception;

	public static final class Empty implements Formatter<Void> {

		@Override
		public String format(ObjectHolder<Void> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			throw new Exception();
		}
	}
}

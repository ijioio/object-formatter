package com.ijioio.object.format.util;

import java.util.Collection;

public class MetadataUtil {

	public static Class<?> normalizeType(Class<?> type) {

		if (Number.class.isAssignableFrom(type)) {
			return Number.class;
		}

		if (Collection.class.isAssignableFrom(type)) {
			return Collection.class;
		}

		return type;
	}
}

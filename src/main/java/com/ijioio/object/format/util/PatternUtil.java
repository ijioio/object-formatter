package com.ijioio.object.format.util;

import com.ijioio.object.format.metadata.PropertyMetadata;

public class PatternUtil {

	public static class Pattern {

		// TODO: move to configuration!
		public static final String SEPARATOR = ", ";

		public static String self() {
			return self(null);
		}

		// TODO: make it use configuration!
		public static String self(String pattern) {
			return pattern != null ? String.format("${id=%s|pattern=%s}", PropertyMetadata.THIS, pattern)
					: String.format("${id=%s}", PropertyMetadata.THIS);
		}
	}
}

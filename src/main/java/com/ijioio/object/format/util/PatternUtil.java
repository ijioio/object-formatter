package com.ijioio.object.format.util;

import java.util.Objects;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.metadata.PropertyMetadata;

public class PatternUtil {

	public static class Pattern {

		public static String self() {
			return self(null, Configuration.builder().build());
		}

		public static String self(Configuration configuration) {
			return self(null, configuration);
		}

		public static String self(String pattern, Configuration configuration) {

			Objects.requireNonNull(configuration, "configuration must not be null");

			StringBuilder stringBuilder = new StringBuilder();

			stringBuilder.append(configuration.getParserConfiguration().getVariableStartSequence());
			stringBuilder.append(configuration.getParserConfiguration().getVariablePropertyNameIdSequence());
			stringBuilder.append(configuration.getParserConfiguration().getVariablePropertySeparatorSequence());
			stringBuilder.append(PropertyMetadata.THIS);

			if (pattern != null) {

				stringBuilder.append(configuration.getParserConfiguration().getVariableSeparatorSequence());
				stringBuilder.append(configuration.getParserConfiguration().getVariablePropertyNamePatternSequence());
				stringBuilder.append(configuration.getParserConfiguration().getVariablePropertySeparatorSequence());
				stringBuilder.append(pattern);
			}

			stringBuilder.append(configuration.getParserConfiguration().getVariableEndSequence());

			return stringBuilder.toString();
		}
	}
}

package com.ijioio.object.format;

import java.util.Optional;

import com.ijioio.object.format.Configuration.ParserConfiguration.ParserConfigurationBuilder;

public class Configuration {

	public static ConfigurationBuilder builder() {
		return new ConfigurationBuilder();
	}

	private final ParserConfiguration parserConfiguration;

	private Configuration(ConfigurationBuilder builder) {

		this.parserConfiguration = Optional.ofNullable(builder.parserConfiguration)
				.orElse(ParserConfiguration.builder().build());
	}

	public ParserConfiguration getParserConfiguration() {
		return parserConfiguration;
	}

	public static class ConfigurationBuilder {

		private ParserConfiguration parserConfiguration;

		private ConfigurationBuilder() {
			// Empty
		}

		public ConfigurationBuilder parserConfiguration(ParserConfiguration parserConfiguration) {

			this.parserConfiguration = parserConfiguration;
			return this;
		}

		public ParserConfigurationBuilder parserConfiguration() {
			return ParserConfiguration.builder(this);
		}

		public Configuration build() {
			return new Configuration(this);
		}
	}

	public static class ParserConfiguration {

		private static final String ESCAPE_SEQUENCE_DEFAULT_VALUE = "'";

		private static final String VARIABLE_START_SEQUENCE_DEFAULT_VALUE = "${";

		private static final String VARIABLE_END_SEQUENCE_DEFAULT_VALUE = "}";

		private static final String VARIABLE_SEPARATOR_SEQUENCE_DEFAULT_VALUE = "|";

		private static final String VARIABLE_PROPERTY_SEPARATOR_SEQUENCE_DEFAULT_VALUE = "=";

		public static ParserConfigurationBuilder builder() {
			return new ParserConfigurationBuilder(null);
		}

		public static ParserConfigurationBuilder builder(ConfigurationBuilder parent) {
			return new ParserConfigurationBuilder(parent);
		}

		private final String escapeSequence;

		private final String variableStartSequence;

		private final String variableEndSequence;

		private final String variableSeparatorSequence;

		private final String variablePropertySeparatorSequence;

		private ParserConfiguration(ParserConfigurationBuilder builder) {

			this.escapeSequence = Optional.ofNullable(builder.escapeSequence).orElse(ESCAPE_SEQUENCE_DEFAULT_VALUE);
			this.variableStartSequence = Optional.ofNullable(builder.variableStartSequence)
					.orElse(VARIABLE_START_SEQUENCE_DEFAULT_VALUE);
			this.variableEndSequence = Optional.ofNullable(builder.variableEndSequence)
					.orElse(VARIABLE_END_SEQUENCE_DEFAULT_VALUE);
			this.variableSeparatorSequence = Optional.ofNullable(builder.variableSeparatorSequence)
					.orElse(VARIABLE_SEPARATOR_SEQUENCE_DEFAULT_VALUE);
			this.variablePropertySeparatorSequence = Optional.ofNullable(builder.variablePropertySeparatorSequence)
					.orElse(VARIABLE_PROPERTY_SEPARATOR_SEQUENCE_DEFAULT_VALUE);

			// TODO: validate not empty, not contains substring, etc.
		}

		public String getEscapeSequence() {
			return escapeSequence;
		}

		public String getVariableStartSequence() {
			return variableStartSequence;
		}

		public String getVariableEndSequence() {
			return variableEndSequence;
		}

		public String getVariableSeparatorSequence() {
			return variableSeparatorSequence;
		}

		public String getVariablePropertySeparatorSequence() {
			return variablePropertySeparatorSequence;
		}

		public static class ParserConfigurationBuilder {

			private final ConfigurationBuilder parent;

			private String escapeSequence;

			private String variableStartSequence;

			private String variableEndSequence;

			private String variableSeparatorSequence;

			private String variablePropertySeparatorSequence;

			private ParserConfigurationBuilder(ConfigurationBuilder parent) {
				this.parent = parent;
			}

			public ParserConfigurationBuilder escapeSequence(String value) {

				escapeSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variableStartSequence(String value) {

				variableStartSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variableEndSequence(String value) {

				variableEndSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variableSeparatorSequence(String value) {

				variableSeparatorSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variablePropertySeparatorSequence(String value) {

				variablePropertySeparatorSequence = value;
				return this;
			}

			public ConfigurationBuilder end() {

				parent.parserConfiguration(build());
				return parent;
			}

			public ParserConfiguration build() {
				return new ParserConfiguration(this);
			}
		}
	}
}

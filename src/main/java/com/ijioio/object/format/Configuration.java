package com.ijioio.object.format;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.ijioio.object.format.Configuration.DelegateConfiguration.DelegateConfigurationBuilder;
import com.ijioio.object.format.Configuration.ParserConfiguration.ParserConfigurationBuilder;

public class Configuration {

	public static ConfigurationBuilder builder() {
		return new ConfigurationBuilder();
	}

	private final ParserConfiguration parserConfiguration;

	private final DelegateConfiguration delegateConfiguration;

	private Configuration(ConfigurationBuilder builder) {

		this.parserConfiguration = Optional.ofNullable(builder.parserConfiguration)
				.orElse(ParserConfiguration.builder().build());
		this.delegateConfiguration = Optional.ofNullable(builder.delegateConfiguration)
				.orElse(DelegateConfiguration.builder().build());
	}

	public ParserConfiguration getParserConfiguration() {
		return parserConfiguration;
	}

	public DelegateConfiguration getDelegateConfiguration() {
		return delegateConfiguration;
	}

	public static class ConfigurationBuilder {

		private ParserConfiguration parserConfiguration;

		private DelegateConfiguration delegateConfiguration;

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

		public ConfigurationBuilder delegateConfiguration(DelegateConfiguration delegateConfiguration) {

			this.delegateConfiguration = delegateConfiguration;
			return this;
		}

		public DelegateConfigurationBuilder delegateConfiguration() {
			return DelegateConfiguration.builder(this);
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

		private static final String VARIABLE_PROPERTY_NAME_ID_SEQUENCE_DEFAULT_VALUE = "id";

		private static final String VARIABLE_PROPERTY_NAME_PADDING_SEQUENCE_DEFAULT_VALUE = "padding";

		private static final String VARIABLE_PROPERTY_NAME_PREFIX_SEQUENCE_DEFAULT_VALUE = "prefix";

		private static final String VARIABLE_PROPERTY_NAME_SUFFIX_SEQUENCE_DEFAULT_VALUE = "suffix";

		private static final String VARIABLE_PROPERTY_NAME_SEPARATOR_SEQUENCE_DEFAULT_VALUE = "separator";

		private static final String VARIABLE_PROPERTY_NAME_PATTERN_SEQUENCE_DEFAULT_VALUE = "pattern";

		private static final String VARIABLE_PROPERTY_NAME_DEFAULT_SEQUENCE_DEFAULT_VALUE = "default";

		private static final String VARIABLE_PROPERTY_VALUE_SEPARATOR_SEQUENCE_DEFAULT_VALUE = ":";

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

		private final String variablePropertyNameIdSequence;

		private final String variablePropertyNamePaddingSequence;

		private final String variablePropertyNamePrefixSequence;

		private final String variablePropertyNameSuffixSequence;

		private final String variablePropertyNameSeparatorSequence;

		private final String variablePropertyNamePatternSequence;

		private final String variablePropertyNameDefaultSequence;

		private final String variablePropertyValueSeparatorSequence;

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
			this.variablePropertyNameIdSequence = Optional.ofNullable(builder.variablePropertyNameIdSequence)
					.orElse(VARIABLE_PROPERTY_NAME_ID_SEQUENCE_DEFAULT_VALUE);
			this.variablePropertyNamePaddingSequence = Optional.ofNullable(builder.variablePropertyNamePaddingSequence)
					.orElse(VARIABLE_PROPERTY_NAME_PADDING_SEQUENCE_DEFAULT_VALUE);
			this.variablePropertyNamePrefixSequence = Optional.ofNullable(builder.variablePropertyNamePrefixSequence)
					.orElse(VARIABLE_PROPERTY_NAME_PREFIX_SEQUENCE_DEFAULT_VALUE);
			this.variablePropertyNameSuffixSequence = Optional.ofNullable(builder.variablePropertyNameSuffixSequence)
					.orElse(VARIABLE_PROPERTY_NAME_SUFFIX_SEQUENCE_DEFAULT_VALUE);
			this.variablePropertyNameSeparatorSequence = Optional
					.ofNullable(builder.variablePropertyNameSeparatorSequence)
					.orElse(VARIABLE_PROPERTY_NAME_SEPARATOR_SEQUENCE_DEFAULT_VALUE);
			this.variablePropertyNamePatternSequence = Optional.ofNullable(builder.variablePropertyNamePatternSequence)
					.orElse(VARIABLE_PROPERTY_NAME_PATTERN_SEQUENCE_DEFAULT_VALUE);
			this.variablePropertyNameDefaultSequence = Optional.ofNullable(builder.variablePropertyNameDefaultSequence)
					.orElse(VARIABLE_PROPERTY_NAME_DEFAULT_SEQUENCE_DEFAULT_VALUE);
			this.variablePropertyValueSeparatorSequence = Optional
					.ofNullable(builder.variablePropertyValueSeparatorSequence)
					.orElse(VARIABLE_PROPERTY_VALUE_SEPARATOR_SEQUENCE_DEFAULT_VALUE);

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

		public String getVariablePropertyNameIdSequence() {
			return variablePropertyNameIdSequence;
		}

		public String getVariablePropertyNamePaddingSequence() {
			return variablePropertyNamePaddingSequence;
		}

		public String getVariablePropertyNamePrefixSequence() {
			return variablePropertyNamePrefixSequence;
		}

		public String getVariablePropertyNameSuffixSequence() {
			return variablePropertyNameSuffixSequence;
		}

		public String getVariablePropertyNameSeparatorSequence() {
			return variablePropertyNameSeparatorSequence;
		}

		public String getVariablePropertyNamePatternSequence() {
			return variablePropertyNamePatternSequence;
		}

		public String getVariablePropertyNameDefaultSequence() {
			return variablePropertyNameDefaultSequence;
		}

		public String getVariablePropertyValueSeparatorSequence() {
			return variablePropertyValueSeparatorSequence;
		}

		public static class ParserConfigurationBuilder {

			private final ConfigurationBuilder parent;

			private String escapeSequence;

			private String variableStartSequence;

			private String variableEndSequence;

			private String variableSeparatorSequence;

			private String variablePropertySeparatorSequence;

			private String variablePropertyNameIdSequence;

			private String variablePropertyNamePaddingSequence;

			private String variablePropertyNamePrefixSequence;

			private String variablePropertyNameSuffixSequence;

			private String variablePropertyNameSeparatorSequence;

			private String variablePropertyNamePatternSequence;

			private String variablePropertyNameDefaultSequence;

			private String variablePropertyValueSeparatorSequence;

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

			public ParserConfigurationBuilder variablePropertyNameIdSequence(String value) {

				variablePropertyNameIdSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variablePropertyNamePaddingSequence(String value) {

				variablePropertyNamePaddingSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variablePropertyNamePrefixSequence(String value) {

				variablePropertyNamePrefixSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variablePropertyNameSuffixSequence(String value) {

				variablePropertyNameSuffixSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variablePropertyNameSeparatorSequence(String value) {

				variablePropertyNameSeparatorSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variablePropertyNamePatternSequence(String value) {

				variablePropertyNamePatternSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variablePropertyNameDefaultSequence(String value) {

				variablePropertyNameDefaultSequence = value;
				return this;
			}

			public ParserConfigurationBuilder variablePropertyValueSeparatorSequence(String value) {

				variablePropertyValueSeparatorSequence = value;
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

	public static class DelegateConfiguration {

		public static DelegateConfigurationBuilder builder() {
			return new DelegateConfigurationBuilder(null);
		}

		public static DelegateConfigurationBuilder builder(ConfigurationBuilder parent) {
			return new DelegateConfigurationBuilder(parent);
		}

		private final Map<Class<?>, Class<?>> delegates = new HashMap<>();

		private DelegateConfiguration(DelegateConfigurationBuilder builder) {

			this.delegates.clear();
			this.delegates.putAll(builder.delegates);

			// TODO: validate not null keys/values
		}

		public Map<Class<?>, Class<?>> getDelegates() {
			return Collections.unmodifiableMap(delegates);
		}

		public static class DelegateConfigurationBuilder {

			private final ConfigurationBuilder parent;

			private final Map<Class<?>, Class<?>> delegates = new HashMap<>();

			private DelegateConfigurationBuilder(ConfigurationBuilder parent) {
				this.parent = parent;
			}

			public DelegateConfigurationBuilder delegate(Class<?> type, Class<?> delegateType) {

				delegates.put(type, delegateType);
				return this;
			}

			public ConfigurationBuilder end() {

				parent.delegateConfiguration(build());
				return parent;
			}

			public DelegateConfiguration build() {
				return new DelegateConfiguration(this);
			}
		}
	}
}

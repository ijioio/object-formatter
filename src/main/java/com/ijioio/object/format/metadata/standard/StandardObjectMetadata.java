package com.ijioio.object.format.metadata.standard;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ijioio.object.format.annotation.FormatType;
import com.ijioio.object.format.extractor.Extractor;
import com.ijioio.object.format.formatter.Formatter;
import com.ijioio.object.format.metadata.ObjectMetadata;
import com.ijioio.object.format.metadata.PropertyMetadata;
import com.ijioio.object.format.util.MetadataUtil;

public class StandardObjectMetadata implements ObjectMetadata {

	private final Class<?> type;

	private final Class<?> delegateType;

	private Class<? extends Formatter<?>> formatter;

	private final Set<String> aliases = new HashSet<>();

	private final List<PropertyMetadata> properties = new ArrayList<>();

	public StandardObjectMetadata(Class<?> type, Class<?> delegateType, Class<? extends Formatter<?>> formatter) {

		Objects.requireNonNull(type, "type must not be null");

		this.type = type;
		this.delegateType = delegateType;
		this.formatter = formatter;

		init();
	}

	private void init() {

		Set<String> aliases = new HashSet<>();

		Class<? extends Formatter<?>> formatter = null;

		FormatType formatType = type.getAnnotation(FormatType.class);

		if (formatType != null) {

			String alias = formatType.value();

			if (alias != null) {
				aliases.add(alias);
			}

			formatter = !formatType.formatter().equals(Formatter.Empty.class) ? formatType.formatter() : null;
		}

		if (delegateType != null) {

			formatType = delegateType.getAnnotation(FormatType.class);

			if (formatType != null) {

				String alias = formatType.value();

				if (alias != null) {
					aliases.add(alias);
				}

				formatter = !formatType.formatter().equals(Formatter.Empty.class) ? formatType.formatter() : null;
			}
		}

		this.aliases.clear();
		this.aliases.addAll(aliases);

		if (this.formatter == null) {
			this.formatter = formatter;
		}

		Map<String, Field> fields = new HashMap<>();

		Class<?> parentType = type;

		// Collect all the fields of the type including parent fields
		while (parentType != null) {

			for (Field field : parentType.getDeclaredFields()) {

				if (field.isSynthetic()) {
					continue;
				}

				String name = field.getName();

				// In case of parent has field with the same name
				// as child field then child field take precedence
				if (!fields.containsKey(name)) {
					fields.put(name, field);
				}
			}

			parentType = parentType.getSuperclass();
		}

		Map<String, Field> delegateFields = new HashMap<>();

		// Collect all the fields of the delegate type
		if (delegateType != null) {

			for (Field delegateField : delegateType.getDeclaredFields()) {

				if (delegateField.isSynthetic()) {
					continue;
				}

				delegateFields.put(delegateField.getName(), delegateField);
			}
		}

		Set<String> fieldNames = Stream.concat(fields.keySet().stream(), delegateFields.keySet().stream())
				.collect(Collectors.toSet());

		for (String fieldName : fieldNames) {

			Field field = fields.get(fieldName);
			Field delegateField = delegateFields.get(fieldName);

			properties.add(new StandardPropertyMetadata(field, delegateField));
		}

		properties.add(new PropertyMetadata() {

			@Override
			public Class<?> getType() {
				return StandardObjectMetadata.this.getType();
			}

			@Override
			public String getId() {
				return THIS;
			}

			@Override
			public Class<? extends Extractor<?>> getExtractor() {
				return null;
			}

			@Override
			public Class<? extends Formatter<?>> getFormatter() {
				return null;
			}

			@Override
			public Set<String> getAliases() {
				return Collections.emptySet();
			}

			@Override
			public Object getValue(Object object) throws Exception {
				return object;
			}
		});
	}

	@Override
	public String getId() {
		return type.getName();
	}

	@Override
	public Class<?> getType() {
		return MetadataUtil.normalizeType(type);
	}

	@Override
	public Class<? extends Formatter<?>> getFormatter() {
		return formatter;
	}

	@Override
	public Set<String> getAliases() {
		return Collections.unmodifiableSet(aliases);
	}

	@Override
	public List<PropertyMetadata> getProperties() {
		return Collections.unmodifiableList(properties);
	}

	@Override
	public String toString() {
		return "StandardObjectMetadata [type=" + type + ", delegateType=" + delegateType + ", aliases=" + aliases
				+ ", properties=" + properties + "]";
	}
}

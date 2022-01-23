package com.ijioio.object.format.metadata.standard;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.ijioio.object.format.annotation.FormatElement;
import com.ijioio.object.format.extractor.Extractor;
import com.ijioio.object.format.formatter.Formatter;
import com.ijioio.object.format.metadata.PropertyMetadata;
import com.ijioio.object.format.util.MetadataUtil;

public class StandardPropertyMetadata implements PropertyMetadata {

	private final Field field;

	private final Field delegateField;

	private Class<? extends Extractor<?>> extractor;

	private Class<? extends Formatter<?>> formatter;

	private final Set<String> aliases = new HashSet<>();

	public StandardPropertyMetadata(Field field, Field delegateField) {

		if (field == null && delegateField == null) {
			throw new NullPointerException("either field or delegateField must not be null");
		}

		this.field = field;
		this.delegateField = delegateField;

		init();
	}

	private void init() {

		Set<String> aliases = new HashSet<>();

		Class<? extends Extractor<?>> extractor = null;
		Class<? extends Formatter<?>> formatter = null;

		if (field != null) {

			FormatElement formatElement = field.getAnnotation(FormatElement.class);

			if (formatElement != null) {

				String alias = formatElement.value();

				if (alias != null) {
					aliases.add(alias);
				}

				extractor = !formatElement.extractor().equals(Extractor.Empty.class) ? formatElement.extractor() : null;
				formatter = !formatElement.formatter().equals(Formatter.Empty.class) ? formatElement.formatter() : null;
			}
		}

		if (delegateField != null) {

			FormatElement formatElement = delegateField.getAnnotation(FormatElement.class);

			if (formatElement != null) {

				String alias = formatElement.value();

				if (alias != null) {
					aliases.add(alias);
				}

				extractor = !formatElement.extractor().equals(Extractor.Empty.class) ? formatElement.extractor() : null;
				formatter = !formatElement.formatter().equals(Formatter.Empty.class) ? formatElement.formatter() : null;
			}
		}

		this.aliases.clear();
		this.aliases.addAll(aliases);

		this.extractor = extractor;
		this.formatter = formatter;
	}

	@Override
	public String getId() {
		return field != null ? field.getName() : delegateField.getName();
	}

	@Override
	public Class<?> getType() {
		return MetadataUtil.normalizeType(field != null ? field.getType() : delegateField.getType());
	}

	@Override
	public Class<? extends Extractor<?>> getExtractor() {
		return extractor;
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
	public Object getValue(Object object) throws Exception {

		if (field != null) {

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			return field.get(object);
		}

		if (!delegateField.isAccessible()) {
			delegateField.setAccessible(true);
		}

		return delegateField.get(object);
	}

	@Override
	public String toString() {
		return "StandardPropertyMetadata [field=" + field + ", delegateField=" + delegateField + ", aliases=" + aliases
				+ "]";
	}
}

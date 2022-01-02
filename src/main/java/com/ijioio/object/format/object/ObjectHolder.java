package com.ijioio.object.format.object;

import java.util.Objects;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.formatter.Formatter;
import com.ijioio.object.format.metadata.ObjectMetadata;
import com.ijioio.object.format.metadata.standard.StandardObjectMetadata;

public class ObjectHolder<V> {

	private final V object;

	private final ObjectHolder<?> parent;

	private final ObjectMetadata metadata;

	public static <V> ObjectHolder<V> of(V object) {
		return new ObjectHolder<V>(object, null, Configuration.builder().build(), null);
	}

	public static <V> ObjectHolder<V> of(V object, Configuration configuration) {
		return new ObjectHolder<V>(object, null, configuration, null);
	}

	public static <V> ObjectHolder<V> of(V object, ObjectHolder<?> parent) {
		return new ObjectHolder<V>(object, parent, Configuration.builder().build(), null);
	}

	public static <V> ObjectHolder<V> of(V object, ObjectHolder<?> parent, Configuration configuration) {
		return new ObjectHolder<V>(object, parent, configuration, null);
	}

	private ObjectHolder(V object, ObjectHolder<?> parent, Configuration configuration,
			Class<? extends Formatter<?>> formatter) {

		Objects.requireNonNull(object, "object must not be null");
		Objects.requireNonNull(configuration, "configuration must not be null");

		this.object = object;
		this.parent = parent;

		Class<?> type = object.getClass();
		Class<?> delegateType = configuration.getDelegateConfiguration().getDelegates().get(type);

		metadata = new StandardObjectMetadata(type, delegateType, formatter);
	}

	public V getObject() {
		return object;
	}

	public ObjectHolder<?> getParent() {
		return parent;
	}

	public ObjectMetadata getMetadata() {
		return metadata;
	}
}

package com.ijioio.object.format.test.builder;

import java.util.function.Consumer;

import com.ijioio.object.format.test.builder.PassengerBuilder.PassengerPrototype;

public class PassengerBuilder<P extends PassengerPrototype, B extends Builder<?, ?>> implements Builder<P, B> {

	public static interface PassengerPrototype {

		public void setFirstName(String firstName);

		public String getFirstName();

		public void setLastName(String lastName);

		public String getLastName();
	}

	public static abstract class PassengerPrototypeAdapter implements PassengerPrototype {

		@Override
		public void setFirstName(String firstName) {
			// Empty
		}

		@Override
		public String getFirstName() {
			return null;
		}

		@Override
		public void setLastName(String lastName) {
			// Empty
		}

		@Override
		public String getLastName() {
			return null;
		}
	}

	public static <P extends PassengerPrototype, B extends Builder<?, ?>> PassengerBuilder<P, B> of(Class<P> type) {
		return new PassengerBuilder<P, B>(type, null, null);
	}

	public static <P extends PassengerPrototype, B extends Builder<?, ?>> PassengerBuilder<P, B> of(Class<P> type,
			B parent, Consumer<P> setter) {
		return new PassengerBuilder<P, B>(type, parent, setter);
	}

	private final Class<P> type;

	private final B parent;

	private final Consumer<P> setter;

	private String firstName;

	private String lastName;

	private PassengerBuilder(Class<P> type, B parent, Consumer<P> setter) {

		this.type = type;
		this.parent = parent;
		this.setter = setter;
	}

	public PassengerBuilder<P, B> firstName(String firstName) {

		this.firstName = firstName;
		return this;
	}

	public PassengerBuilder<P, B> lastName(String lastName) {

		this.lastName = lastName;
		return this;
	}

	@Override
	public B end() {

		if (setter != null) {
			setter.accept(build());
		}

		return parent;
	}

	@Override
	public P build() throws BuilderException {

		try {

			P p = type.newInstance();

			p.setFirstName(firstName);
			p.setLastName(lastName);

			return p;

		} catch (InstantiationException | IllegalAccessException e) {

			throw new BuilderException(String.format("object of type %s build failed", type), e);
		}
	}
}

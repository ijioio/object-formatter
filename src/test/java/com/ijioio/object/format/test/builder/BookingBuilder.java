package com.ijioio.object.format.test.builder;

import java.util.function.Consumer;

import com.ijioio.object.format.test.builder.BookingBuilder.BookingPrototype;
import com.ijioio.object.format.test.builder.PassengerBuilder.PassengerPrototype;

public class BookingBuilder<P extends BookingPrototype<E>, E extends PassengerPrototype, B extends Builder<?, ?>>
		implements Builder<P, B> {

	public static interface BookingPrototype<P extends PassengerPrototype> {

		public void setNumber(String number);

		public String getNumber();

		public void setPassenger(P passenger);

		public P getPassenger();
	}

	public static abstract class BookingPrototypeAdapter<P extends PassengerPrototype> implements BookingPrototype<P> {

		@Override
		public void setNumber(String number) {
			// Empty
		}

		@Override
		public String getNumber() {
			return null;
		}

		@Override
		public void setPassenger(P passenger) {
			// Empty
		}

		@Override
		public P getPassenger() {
			return null;
		}
	}

	public static <P extends BookingPrototype<E>, E extends PassengerPrototype, B extends Builder<?, ?>> BookingBuilder<P, E, B> of(
			Class<P> type) {
		return new BookingBuilder<P, E, B>(type, null, null);
	}

	public static <P extends BookingPrototype<E>, E extends PassengerPrototype, B extends Builder<?, ?>> BookingBuilder<P, E, B> of(
			Class<P> type, B parent, Consumer<P> setter) {
		return new BookingBuilder<P, E, B>(type, parent, setter);
	}

	private final Class<P> type;

	private final B parent;

	private final Consumer<P> setter;

	private String number;

	private E passenger;

	private BookingBuilder(Class<P> type, B parent, Consumer<P> setter) {

		this.type = type;
		this.parent = parent;
		this.setter = setter;
	}

	public BookingBuilder<P, E, B> number(String number) {

		this.number = number;
		return this;
	}

	public BookingBuilder<P, E, B> passenger(E passenger) {

		this.passenger = passenger;
		return this;
	}

	public PassengerBuilder<E, BookingBuilder<P, E, B>> passenger(Class<E> passengerType) {

		PassengerBuilder<E, BookingBuilder<P, E, B>> builder = PassengerBuilder.of(passengerType, this,
				item -> passenger(item));

		return builder;
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

			p.setNumber(number);
			p.setPassenger(passenger);

			return p;

		} catch (InstantiationException | IllegalAccessException e) {

			throw new BuilderException(String.format("object of type %s build failed", type), e);
		}
	}
}

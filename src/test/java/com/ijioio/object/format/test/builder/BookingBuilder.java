package com.ijioio.object.format.test.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.ijioio.object.format.test.builder.BookingBuilder.BookingPrototype;
import com.ijioio.object.format.test.builder.PassengerBuilder.PassengerPrototype;
import com.ijioio.object.format.test.builder.SegmentBuilder.SegmentPrototype;

public class BookingBuilder<P extends BookingPrototype<E, S>, E extends PassengerPrototype, S extends SegmentPrototype, B extends Builder<?, ?>>
		implements Builder<P, B> {

	public static interface BookingPrototype<P extends PassengerPrototype, S extends SegmentPrototype> {

		public void setNumber(String number);

		public String getNumber();

		public void setPassenger(P passenger);

		public P getPassenger();

		public Collection<S> getSegments();
	}

	public static abstract class BookingPrototypeAdapter<P extends PassengerPrototype, S extends SegmentPrototype>
			implements BookingPrototype<P, S> {

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

		@Override
		public Collection<S> getSegments() {
			return Collections.emptyList();
		}
	}

	public static <P extends BookingPrototype<E, S>, E extends PassengerPrototype, S extends SegmentPrototype, B extends Builder<?, ?>> BookingBuilder<P, E, S, B> of(
			Class<P> type) {
		return new BookingBuilder<P, E, S, B>(type, null, null);
	}

	public static <P extends BookingPrototype<E, S>, E extends PassengerPrototype, S extends SegmentPrototype, B extends Builder<?, ?>> BookingBuilder<P, E, S, B> of(
			Class<P> type, B parent, Consumer<P> setter) {
		return new BookingBuilder<P, E, S, B>(type, parent, setter);
	}

	private final Class<P> type;

	private final B parent;

	private final Consumer<P> setter;

	private String number;

	private E passenger;

	private List<S> segments = new ArrayList<>();

	private BookingBuilder(Class<P> type, B parent, Consumer<P> setter) {

		this.type = type;
		this.parent = parent;
		this.setter = setter;
	}

	public BookingBuilder<P, E, S, B> number(String number) {

		this.number = number;
		return this;
	}

	public BookingBuilder<P, E, S, B> passenger(E passenger) {

		this.passenger = passenger;
		return this;
	}

	public PassengerBuilder<E, BookingBuilder<P, E, S, B>> passenger(Class<E> passengerType) {

		PassengerBuilder<E, BookingBuilder<P, E, S, B>> builder = PassengerBuilder.of(passengerType, this,
				item -> passenger(item));

		return builder;
	}

	public BookingBuilder<P, E, S, B> segment(S segment) {

		this.segments.add(segment);
		return this;
	}

	public SegmentBuilder<S, BookingBuilder<P, E, S, B>> segment(Class<S> segmentType) {

		SegmentBuilder<S, BookingBuilder<P, E, S, B>> builder = SegmentBuilder.of(segmentType, this,
				item -> segment(item));

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

			p.getSegments().clear();
			p.getSegments().addAll(segments);

			return p;

		} catch (InstantiationException | IllegalAccessException e) {

			throw new BuilderException(String.format("object of type %s build failed", type), e);
		}
	}
}

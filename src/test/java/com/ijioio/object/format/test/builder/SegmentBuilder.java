package com.ijioio.object.format.test.builder;

import java.util.function.Consumer;

import com.ijioio.object.format.test.builder.SegmentBuilder.SegmentPrototype;

public class SegmentBuilder<P extends SegmentPrototype, B extends Builder<?, ?>> implements Builder<P, B> {

	public static interface SegmentPrototype {

		public void setPnr(String pnr);

		public String getPnr();
	}

	public static abstract class SegmentPrototypeAdapter implements SegmentPrototype {

		@Override
		public void setPnr(String pnr) {
			// Empty
		}

		@Override
		public String getPnr() {
			return null;
		}
	}

	public static <P extends SegmentPrototype, B extends Builder<?, ?>> SegmentBuilder<P, B> of(Class<P> type) {
		return new SegmentBuilder<P, B>(type, null, null);
	}

	public static <P extends SegmentPrototype, B extends Builder<?, ?>> SegmentBuilder<P, B> of(Class<P> type, B parent,
			Consumer<P> setter) {
		return new SegmentBuilder<P, B>(type, parent, setter);
	}

	private final Class<P> type;

	private final B parent;

	private final Consumer<P> setter;

	private String pnr;

	private SegmentBuilder(Class<P> type, B parent, Consumer<P> setter) {

		this.type = type;
		this.parent = parent;
		this.setter = setter;
	}

	public SegmentBuilder<P, B> pnr(String pnr) {

		this.pnr = pnr;
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

			p.setPnr(pnr);

			return p;

		} catch (InstantiationException | IllegalAccessException e) {

			throw new BuilderException(String.format("object of type %s build failed", type), e);
		}
	}
}

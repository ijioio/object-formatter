package com.ijioio.object.format.converter;

public interface Converter<V, R> {

	public Class<R> getType();

	public R convert(V value) throws Exception;

	public static final class Empty implements Converter<Void, Void> {

		@Override
		public Class<Void> getType() {
			return Void.class;
		}

		@Override
		public Void convert(Void value) throws Exception {
			throw new Exception();
		}
	}
}

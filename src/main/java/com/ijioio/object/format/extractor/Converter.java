package com.ijioio.object.format.extractor;

public interface Converter<V> {

	public Object convert(V value) throws Exception;

	public static final class Empty implements Converter<Void> {

		@Override
		public Object convert(Void value) throws Exception {
			throw new Exception();
		}
	}
}

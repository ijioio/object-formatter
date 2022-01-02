package com.ijioio.object.format.extractor;

public interface Extractor<V> {

	public Object extract(V owner) throws Exception;

	public static final class Default implements Extractor<Void> {

		@Override
		public Object extract(Void owner) throws Exception {
			throw new Exception();
		}
	}
}

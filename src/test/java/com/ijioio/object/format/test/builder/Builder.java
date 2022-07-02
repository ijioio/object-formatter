package com.ijioio.object.format.test.builder;

public interface Builder<P, B extends Builder<?, ?>> {

	public B end();

	public P build() throws BuilderException;
}
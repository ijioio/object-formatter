package com.ijioio.object.format.exception;

public class FormatException extends RuntimeException {

	private static final long serialVersionUID = 7066255632583484859L;

	public FormatException(final String message) {
		super(message);
	}

	public FormatException(final Throwable cause) {
		super(cause);
	}

	public FormatException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
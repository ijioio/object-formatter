package com.ijioio.object.format.util;

/** Helper class for text. */
public class TextUtil {

	/**
	 * Checks whether an indicated {@code value} is blank, i.e. either {@code null}
	 * or zero length or contains only the white space characters.
	 *
	 * @param value to check
	 * @return {@code true} if value is blank, {@code false} otherwise
	 */
	public static boolean isBlank(CharSequence value) {
		return value == null || value.toString().trim().isEmpty();
	}

	/**
	 * Checks whether an indicated {@code value} is empty, i.e. either {@code null}
	 * or zero length.
	 *
	 * @param value to check
	 * @return {@code true} if value is empty, {@code false} otherwise
	 */
	public static boolean isEmpty(CharSequence value) {
		return value == null || value.toString().isEmpty();
	}
}

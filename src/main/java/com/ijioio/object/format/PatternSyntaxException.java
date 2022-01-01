package com.ijioio.object.format;

public class PatternSyntaxException extends IllegalArgumentException {

	private static final long serialVersionUID = 777679550037206037L;

	private final String description;

	private final String pattern;

	private final int index;

	public PatternSyntaxException(final String description, final String pattern, final int index) {

		this.description = description;
		this.pattern = pattern;
		this.index = index;
	}

	public String getDescription() {
		return description;
	}

	public String getPattern() {
		return pattern;
	}

	public int getIndex() {
		return index;
	}

	@Override
	public String getMessage() {

		StringBuilder message = new StringBuilder();

		message.append(description);

		if (index >= 0) {

			message.append(" near index ");
			message.append(index);
		}

		message.append(System.lineSeparator());
		message.append(pattern);

		if (index >= 0 && pattern != null && index < pattern.length()) {

			message.append(System.lineSeparator());

			for (int i = 0; i < index; i++) {
				message.append(' ');
			}

			message.append('^');
		}

		return message.toString();
	}
}
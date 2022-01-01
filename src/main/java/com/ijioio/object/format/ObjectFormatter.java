package com.ijioio.object.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ijioio.object.format.util.DebugUtil;
import com.ijioio.object.format.util.TextUtil;
import com.ijioio.object.format.util.TupleUtil.Pair;

public class ObjectFormatter {

	private static final Logger logger = LoggerFactory.getLogger(ObjectFormatter.class);

	private final String pattern;

	private final Configuration configuration;

	private final Entry root;

	/**
	 * Creates object formatter using indicated {@code pattern} and default
	 * configuration. The object formatter returned is immutable and thread safe.
	 * 
	 * @param pattern to use, not null
	 * @return the formatter based on the pattern, not null
	 * @throws PatternSyntaxException if the pattern is invalid
	 */
	public static ObjectFormatter of(final String pattern) {
		return new ObjectFormatter(pattern, Configuration.builder().build());
	}

	/**
	 * Creates object formatter using indicated {@code pattern} and provided
	 * {@code configuration}. The object formatter returned is immutable and thread
	 * safe.
	 * 
	 * @param pattern       to use, not null
	 * @param configuration to use, not null
	 * @return the formatter based on the pattern, not null
	 * @throws PatternSyntaxException if the pattern is invalid
	 */
	public static ObjectFormatter of(final String pattern, Configuration configuration) {
		return new ObjectFormatter(pattern, configuration);
	}

	/**
	 * Constructor to create object formatter instances.
	 * 
	 * @param pattern       to use, not null
	 * @param configuration to use, not null
	 * @see {@link ObjectFormatter#of(String)}
	 */
	private ObjectFormatter(final String pattern, Configuration configuration) {

		Objects.requireNonNull(pattern, "pattern must not be null");
		Objects.requireNonNull(configuration, "configuration must not be null");

		this.pattern = pattern;
		this.configuration = configuration;

		root = new CompoundEntry(pattern, 0, pattern.length(), configuration);

		dump();
	}

	/**
	 * Dumps formatter internal state to the log.
	 */
	private void dump() {

		if (logger.isDebugEnabled()) {

			logger.debug("configuration -> " + configuration);
			logger.debug("pattern -> " + pattern + "\n"
					+ DebugUtil.tree(Collections.singleton(Pair.of((String) null, root)), entry -> {

						if (entry.getSecond() instanceof CompoundEntry) {

							return ((CompoundEntry) entry.getSecond()).entries.stream()
									.map(item -> Pair.of((String) null, item)).collect(Collectors.toList());

						} else if (entry.getSecond() instanceof VariableEntry) {

							Collection<Pair<String, Entry>> entries = new ArrayList<>();

							Optional.ofNullable(((VariableEntry) entry.getSecond()).id)
									.ifPresent(item -> entries.add(Pair.of("id", item)));
							Optional.ofNullable(((VariableEntry) entry.getSecond()).padding)
									.ifPresent(item -> entries.add(Pair.of("padding", item)));
							Optional.ofNullable(((VariableEntry) entry.getSecond()).prefix)
									.ifPresent(item -> entries.add(Pair.of("prefix", item)));
							Optional.ofNullable(((VariableEntry) entry.getSecond()).suffix)
									.ifPresent(item -> entries.add(Pair.of("suffix", item)));
							Optional.ofNullable(((VariableEntry) entry.getSecond()).separator)
									.ifPresent(item -> entries.add(Pair.of("separator", item)));
							Optional.ofNullable(((VariableEntry) entry.getSecond()).patternn)
									.ifPresent(item -> entries.add(Pair.of("pattern", item)));
							Optional.ofNullable(((VariableEntry) entry.getSecond()).defaultt)
									.ifPresent(item -> entries.add(Pair.of("default", item)));

							return entries;

						} else if (entry.getSecond() instanceof SimpleEntry) {

							return Collections.emptyList();
						}

						return Collections.emptyList();

					}, item -> {

						if (item.getSecond() instanceof CompoundEntry) {
							return item.getFirst() != null ? String.format("[COM] %s", item.getFirst()) : "[COM]";
						} else if (item.getSecond() instanceof VariableEntry) {
							return item.getFirst() != null ? String.format("[VAR] %s", item.getFirst()) : "[VAR]";
						} else if (item.getSecond() instanceof SimpleEntry) {
							return item.getFirst() != null
									? String.format("[TXT] %s -> %s", item.getFirst(),
											((SimpleEntry) item.getSecond()).value)
									: String.format("[TXT] %s", ((SimpleEntry) item.getSecond()).value);
						}

						return "";
					}));
		}
	}

	/**
	 * Type of entry.
	 * 
	 * @author Artem Abeleshev
	 *
	 */
	enum Type {

		SIMPLE, VARIABLE;
	}

	/**
	 * Interface representing a pattern entry. Each pattern consists of multiple
	 * pattern entries. Pattern entries can be located next to each other or be
	 * nested. Before pattern can be used it should be parsed. Parsing implies
	 * splitting pattern on entries and building a pattern entries tree.
	 * 
	 * @author Artem Abeleshev
	 */
	interface Entry {

		// TODO: add format method!
	}

	static abstract class BaseEntry implements Entry {

		protected final Configuration configuration;

		protected final String escapeSequence;

		protected final String variableStartSequence;

		protected final String variableEndSequence;

		protected final String variableSeparatorSequence;

		protected final String variablePropertySeparatorSequence;

		BaseEntry(Configuration configuration) {

			this.configuration = configuration;

			this.escapeSequence = configuration.getParserConfiguration().getEscapeSequence();
			this.variableStartSequence = configuration.getParserConfiguration().getVariableStartSequence();
			this.variableEndSequence = configuration.getParserConfiguration().getVariableEndSequence();
			this.variableSeparatorSequence = configuration.getParserConfiguration().getVariableSeparatorSequence();
			this.variablePropertySeparatorSequence = configuration.getParserConfiguration()
					.getVariablePropertySeparatorSequence();
		}

		public String unescape(final String pattern) {

			StringBuilder result = new StringBuilder();

			boolean escape = false;

			for (int i = 0; i < pattern.length(); i++) {

				// If current character is escape character
				if (pattern.regionMatches(i, escapeSequence, 0, escapeSequence.length())) {

					if (escape) {

						// Drop escape sequence flag
						escape = false;

						// Shift index
						i += escapeSequence.length() - 1;

					} else {

						// If next character is exists
						// and it is an escape character
						if (pattern.regionMatches(i + escapeSequence.length(), escapeSequence, 0,
								escapeSequence.length())) {

							// Append escape character
							result.append(escapeSequence);

							// Shift index
							i += escapeSequence.length() * 2 - 1;

						} else {

							// Rise escape sequence flag
							escape = true;

							// Shift index
							i += escapeSequence.length() - 1;
						}
					}

				} else {

					// Append character
					result.append(pattern.charAt(i));
				}
			}

			return result.toString();
		}
	}

	/**
	 * Compound entry represents a part of the pattern that is consist of other
	 * entries. When formatting an object it will delegate formatting to each of the
	 * entries it consist of individually. Compound pattern entry doesn't have any
	 * special syntax, it can be build by concatenating patterns of entries of other
	 * types. Any unparsed piece of the pattern by default is considered as compound
	 * entry. When parsing compound entry it is scanned and split into other
	 * entries, like {@link SimpleEntry} or {@link VariableEntry}. Split pattern
	 * entries will then become a children entries for compound entry in overall
	 * pattern entries hierarchy.
	 * 
	 * <p>
	 * <b>Example:</b>
	 * 
	 * <p>
	 * Given the compound entry <i>Compound entry with ${name} variable</i> it will
	 * split into three independent pattern entries, two simple entries and one
	 * variable entry:
	 * 
	 * <p>
	 * <img src="./doc-files/compound-entry-pattern.png" />
	 * 
	 * <p>
	 * resulting in the following hierarchy:
	 * 
	 * <p>
	 * <img src="./doc-files/compound-entry-hierarchy.png" />
	 * 
	 * <p>
	 * Note that variable pattern entry has its own hierarchy of children. Each
	 * property of the variable entry will produce separate entry holding compound
	 * pattern entry. See {@link VariableEntry} for more details.
	 * 
	 * @author Artem Abeleshev
	 * @see VariableEntry
	 * @see SimpleEntry
	 */
	static final class CompoundEntry extends BaseEntry {

		private final List<Entry> entries = new ArrayList<>();

		CompoundEntry(final String pattern, final int beginIndex, final int endIndex, Configuration configuration) {

			super(configuration);

			// Parse pattern
			parsePattern(pattern, beginIndex, endIndex);
		}

		private void parsePattern(final String pattern, final int beginIndex, final int endIndex) {

			if (logger.isDebugEnabled()) {
				logger.debug("parsing compound -> " + pattern.substring(beginIndex, endIndex));
			}

			boolean escape = false;
			int escapeBeginIndex = 0;

			int entryBeginIndex = beginIndex;
			int hitCount = 0;

			Type type = null;

			for (int i = beginIndex; i < endIndex; i++) {

				// If we are dealing with escape sequence
				if (escape) {

					// If current character is escape character
					if (pattern.regionMatches(i, escapeSequence, 0, escapeSequence.length())) {

						// Drop escape sequence flag
						escape = false;

						// Shift index
						i += escapeSequence.length() - 1;
					}

				} else {

					// If current character is start variable character
					if (pattern.regionMatches(i, variableStartSequence, 0, variableStartSequence.length())) {

						// If we are dealing with simple type entry
						if (type == Type.SIMPLE) {

							// Add simple type entry
							entries.add(new SimpleEntry(pattern, entryBeginIndex, i, configuration));

							// Indicate that we have finished dealing with entry
							type = null;
						}

						// If we are not dealing with any entry
						if (type == null) {

							// Indicate that we are dealing with variable type entry
							type = Type.VARIABLE;

							// Reset entry start index
							entryBeginIndex = i + variableStartSequence.length();
						}

						// If we are dealing with variable type entry
						if (type == Type.VARIABLE) {

							// Increase hit count
							hitCount++;
						}

						// Shift index
						i += variableStartSequence.length() - 1;

						// If current character is end variable character
					} else if (pattern.regionMatches(i, variableEndSequence, 0, variableEndSequence.length())) {

						// If we are not dealing with any entry
						// or we are dealing with simple type entry
						if ((type == null) || (type == Type.SIMPLE)) {
							throw new PatternSyntaxException("missing start of variable", pattern, i);
						}

						// If we are dealing with variable type entry
						if (type == Type.VARIABLE) {

							// Decrease hit count
							hitCount--;

							// If hit count equals 0
							// meaning we have hit end variable character
							// of the entry we are dealing with
							if (hitCount == 0) {

								// Add variable type entry
								entries.add(new VariableEntry(pattern, entryBeginIndex, i, configuration));

								// Indicate that we have finished dealing with entry
								type = null;
							}
						}

						// Shift index
						i += variableEndSequence.length() - 1;

					} else {

						// If we are not dealing with any entry
						if (type == null) {

							// Indicate that we are dealing with simple type entry
							type = Type.SIMPLE;

							// Reset entry start index
							entryBeginIndex = i;
						}

						// If current character is escape character
						if (pattern.regionMatches(i, escapeSequence, 0, escapeSequence.length())) {

							// Rise escape sequence flag
							escape = true;

							// Reset escape start index
							escapeBeginIndex = i;

							// Shift index
							i += escapeSequence.length() - 1;
						}
					}
				}

				// If this is the last character
				if (i == (endIndex - 1)) {

					// If we are dealing with escape sequence
					if (escape) {

						throw new PatternSyntaxException("missing end of escape sequence", pattern, escapeBeginIndex);

						// If we are dealing with simple type entry
					} else if (type == Type.SIMPLE) {

						// Add simple type entry
						entries.add(new SimpleEntry(pattern, entryBeginIndex, endIndex, configuration));

						// If we are dealing with variable type entry
					} else if (type == Type.VARIABLE) {

						throw new PatternSyntaxException("missing end of variable", pattern, entryBeginIndex);
					}
				}
			}
		}
	}

	/**
	 * Variable entry represents a part of the pattern that is a placeholder for the
	 * object's property value. When formatting an object each variable entry will
	 * be replaced with actual property value extracted from the object. Variable
	 * pattern should be build using special format. The most basic form of variable
	 * pattern is as follows:
	 * 
	 * <pre>
	 * ${name}
	 * </pre>
	 * 
	 * <p>
	 * where {@code name} is the name of the property of the object. Canonical
	 * syntax of variable pattern implies indicating the name of the variable's
	 * property followed by variable's property value. The above example can be
	 * rewritten as:
	 * 
	 * <pre>
	 * ${id=name}
	 * </pre>
	 * 
	 * It is allowed to omit variable's property name only for {@code id} property.
	 * Variable entry can contain multiple properties. In case of multiple
	 * properties needs to be applied they should be separated from each other as
	 * follows:
	 * 
	 * <pre>
	 * ${id=name|default=anonymous}
	 * </pre>
	 * 
	 * All the values of variable entry properties are always considered as a
	 * {@link CompoundEntry} that in turn can contain other nested entries. For
	 * example property value can be defined with another variable:
	 * 
	 * <pre>
	 * ${id=name|default=${id=login}}
	 * </pre>
	 * 
	 * Below is the summary of all properties available for the variable pattern
	 * entry:
	 * 
	 * <p>
	 * <table class="striped">
	 * <caption>Variable pattern entry properties</caption> <thead>
	 * <tr>
	 * <th scope="col">Property</th>
	 * <th scope="col">Description</th>
	 * <th scope="col">Optional</th>
	 * <th scope="col">Default</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td scope="row">id</td>
	 * <td>Name of the object's property</td>
	 * <td>no</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td scope="row">separator</td>
	 * <td>In case of the object's property is of type of collection separator will
	 * be used to concatenate all the collection's values</td>
	 * <td>yes</td>
	 * <td>,</td>
	 * </tr>
	 * <tr>
	 * <td scope="row">pattern</td>
	 * <td>Pattern to use to format object's property</td>
	 * <td>yes</td>
	 * <td>Depends on the type of the property, for some standard types dedicated
	 * pattern can be applied. See {@link Formatter}.</td>
	 * </tr>
	 * <tr>
	 * <td scope="row">default</td>
	 * <td>Value that would be used in case of object's property is {@code null} or
	 * empty</td>
	 * <td>yes</td>
	 * <td></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * <p>
	 * When parsing variable entry it is scanned and split into variable's
	 * properties. Each variable's property value will produce a compound entry that
	 * will then become a child entry for variable entry in overall pattern entries
	 * hierarchy.
	 * 
	 * <p>
	 * <b>Example:</b>
	 * 
	 * <p>
	 * Given the variable entry <i>${id=name|default=${id=login}}</i> it will split
	 * into two properties, {@code id} and {@code default}. Values of each of the
	 * properties would be parsed as compound entries:
	 * 
	 * <p>
	 * <img src="./doc-files/variable-entry-pattern.png" />
	 * 
	 * <p>
	 * resulting in the following hierarchy:
	 * 
	 * <p>
	 * <img src="./doc-files/variable-entry-hierarchy.png" />
	 * 
	 * @author Artem Abeleshev
	 * @see CompoundEntry
	 * @see SimpleEntry
	 */
	static final class VariableEntry extends BaseEntry {

		private Entry id;

		private Entry padding;

		private Entry prefix;

		private Entry suffix;

		private Entry separator;

		private Entry patternn;

		private Entry defaultt;

		VariableEntry(final String pattern, final int beginIndex, final int endIndex, Configuration configuration) {

			super(configuration);

			// Parse pattern
			parsePattern(pattern, beginIndex, endIndex);
		}

		public void parsePattern(final String pattern, final int beginIndex, final int endIndex) {

			if (logger.isDebugEnabled()) {
				logger.debug("parsing variable -> " + pattern.substring(beginIndex, endIndex));
			}

			boolean escape = false;

			int propertyBeginIndex = beginIndex;
			int hitCount = 0;

			Type type = null;

			for (int i = beginIndex; i < endIndex; i++) {

				// If we are dealing with escape sequence
				if (escape) {

					// If current character is escape character
					if (pattern.regionMatches(i, escapeSequence, 0, escapeSequence.length())) {

						// Drop escape sequence flag
						escape = false;

						// Shift index
						i += escapeSequence.length() - 1;
					}

				} else {

					// If current character is start variable character
					if (pattern.regionMatches(i, variableStartSequence, 0, variableStartSequence.length())) {

						// If we are not dealing with any entry
						if (type == null) {

							// Indicate that we are dealing with variable type entry
							type = Type.VARIABLE;
						}

						// If we are dealing with variable type entry
						if (type == Type.VARIABLE) {

							// Increase hit count
							hitCount++;
						}

						// Shift index
						i += variableStartSequence.length() - 1;

						// If current character is end variable character
					} else if (pattern.regionMatches(i, variableEndSequence, 0, variableEndSequence.length())) {

						// If we are dealing with variable type entry
						if (type == Type.VARIABLE) {

							// Decrease hit count
							hitCount--;

							// If hit count equals 0
							// meaning we have hit end variable character
							// of the entry we are dealing with
							if (hitCount == 0) {

								// Indicate that we have finished dealing with entry
								type = null;
							}
						}

						// Shift index
						i += variableEndSequence.length() - 1;

						// If current character is property separator character
					} else if (pattern.regionMatches(i, variableSeparatorSequence, 0,
							variableSeparatorSequence.length())) {

						if (hitCount == 0) {

							// Parse property
							parseProperty(pattern, propertyBeginIndex, i);

							// Reset property begin index
							propertyBeginIndex = i + variableSeparatorSequence.length();
						}

						// Shift index
						i += variableSeparatorSequence.length() - 1;

						// If current character is escape character
					} else if (pattern.regionMatches(i, escapeSequence, 0, escapeSequence.length())) {

						// Rise escape sequence flag
						escape = true;

						// Shift index
						i += escapeSequence.length() - 1;
					}
				}

				// If this is the last character
				if (i == (endIndex - 1)) {

					// Parse property
					parseProperty(pattern, propertyBeginIndex, endIndex);
				}
			}

			if (id == null) {
				throw new PatternSyntaxException("missing mandatory variable property \"id\"",
						pattern, endIndex);
			}
		}

		private void parseProperty(final String pattern, final int beginIndex, final int endIndex) {

			if (logger.isDebugEnabled()) {
				logger.debug("parsing property of variable -> " + pattern.substring(beginIndex, endIndex));
			}

			boolean escape = false;

			for (int i = beginIndex; i < endIndex; i++) {

				// If we are dealing with escape sequence
				if (escape) {

					// If current character is escape character
					if (pattern.regionMatches(i, escapeSequence, 0, escapeSequence.length())) {

						// Drop escape sequence flag
						escape = false;

						// Shift index
						i += escapeSequence.length() - 1;
					}

				} else {

					// If current character is property value separator character
					if (pattern.regionMatches(i, variablePropertySeparatorSequence, 0,
							variablePropertySeparatorSequence.length())) {

						Pair<String, String> property = Pair.of(pattern.substring(beginIndex, i),
								pattern.substring(i + variablePropertySeparatorSequence.length(), endIndex));

						if (TextUtil.isEmpty(property.getFirst())) {
							throw new PatternSyntaxException("missing variable property name", pattern, beginIndex);
						}

						if (TextUtil.isEmpty(property.getSecond())) {
							throw new PatternSyntaxException("missing variable property value", pattern, endIndex);
						}

						String name = unescape(property.getFirst());

						if (name.equals("id")) {

							if (id != null) {
								throw new PatternSyntaxException("id property value already defined", pattern,
										beginIndex);
							}

							id = new CompoundEntry(pattern, i + variablePropertySeparatorSequence.length(), endIndex,
									configuration);

						} else if (name.equals("separator")) {

							if (separator != null) {
								throw new PatternSyntaxException("separator property value already defined", pattern,
										beginIndex);
							}

							separator = new CompoundEntry(pattern, i + variablePropertySeparatorSequence.length(),
									endIndex, configuration);

						} else if (name.equals("pattern")) {

							if (patternn != null) {
								throw new PatternSyntaxException("pattern property value already defined", pattern,
										beginIndex);
							}

							patternn = new CompoundEntry(pattern, i + variablePropertySeparatorSequence.length(),
									endIndex, configuration);

						} else if (name.equals("default")) {

							if (defaultt != null) {
								throw new PatternSyntaxException("default property value already defined", pattern,
										beginIndex);
							}

							defaultt = new CompoundEntry(pattern, i + variablePropertySeparatorSequence.length(),
									endIndex, configuration);

						} else {
							throw new PatternSyntaxException(
									String.format("unsupported variable property \"%s\"", name), pattern, beginIndex);
						}

						// Shift index
						i += variablePropertySeparatorSequence.length() - 1;

						break;

						// If current character is escape character
					} else if (pattern.regionMatches(i, escapeSequence, 0, escapeSequence.length())) {

						// Rise escape sequence flag
						escape = true;

						// Shift index
						i += escapeSequence.length() - 1;
					}
				}

				// If this is the last character
				if (i == (endIndex - 1)) {

					if (id != null) {
						throw new PatternSyntaxException("id property value already defined", pattern, beginIndex);
					}

					id = new CompoundEntry(pattern, beginIndex, endIndex, configuration);
				}
			}
		}
	}

	/**
	 * Simple entry represents a part of the pattern that contains static text. When
	 * formatting an object the pattern of simple entries are applied as it is
	 * without any processing. Simple pattern entry doesn't have any special syntax,
	 * it can be build by using any character sequence:
	 * 
	 * <pre>
	 * Simple entry
	 * </pre>
	 * 
	 * <p>
	 * If simple pattern needs to contain some special character sequences (for
	 * example, character sequences that are used as other entry types delimiters)
	 * they can be escaped with escape character sequence as follows:
	 * 
	 * <pre>
	 * Simple entry with '${' and '}' special character sequences
	 * </pre>
	 *
	 * In case of escape character sequence itself need to be applied it should be
	 * indicated twice:
	 * 
	 * <pre>
	 * Simple entry with '' escape character sequence
	 * </pre>
	 *
	 * <p>
	 * When parsing simple entry it is scanned and unescaped. Simple entry doesn't
	 * produce any children entries so it can be evaluated as a <i>leaf</i> in an
	 * overall overall pattern entries hierarchy.
	 * 
	 * <p>
	 * <b>Example:</b>
	 * 
	 * <p>
	 * Given the simple entry <i>Simple entry with plain text</i> it will be applied
	 * as it is:
	 * 
	 * <p>
	 * <img src="./doc-files/simple-entry-pattern.png" />
	 * 
	 * <p>
	 * resulting in the following hierarchy:
	 * 
	 * <p>
	 * <img src="./doc-files/simple-entry-hierarchy.png" />
	 * 
	 * @author Artem Abeleshev
	 * @see CompoundEntry
	 * @see VariableEntry
	 */
	static final class SimpleEntry extends BaseEntry {

		String value;

		SimpleEntry(final String pattern, final int beginIndex, final int endIndex, Configuration configuration) {

			super(configuration);

			// Parse pattern
			parsePattern(pattern, beginIndex, endIndex);
		}

		private void parsePattern(final String pattern, final int beginIndex, final int endIndex) {

			if (logger.isDebugEnabled()) {
				logger.debug("parsing simple -> " + pattern.substring(beginIndex, endIndex));
			}

			this.value = unescape(pattern.substring(beginIndex, endIndex));
		}
	}
}

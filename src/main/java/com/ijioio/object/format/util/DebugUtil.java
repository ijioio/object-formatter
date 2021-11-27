package com.ijioio.object.format.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DebugUtil {

	/**
	 * Creates string representation of the items tree.
	 *
	 * @param items     to use for root
	 * @param extractor function to extract children of the item
	 * @param renderer  function to render item
	 * @return string representation of the tree
	 */
	public static <T> String tree(final Collection<T> items, final Function<T, Collection<T>> extractor,
			final Function<T, String> renderer) {

		StringBuilder treeBuilder = new StringBuilder();

		tree(treeBuilder, items, extractor, renderer, "", true);

		return treeBuilder.toString();
	}

	private static <T> void tree(final StringBuilder treeBuilder, final Collection<T> items,
			final Function<T, Collection<T>> extractor, final Function<T, String> renderer, final String prefix,
			final boolean root) {

		int count = 0;

		for (T item : items) {

			treeBuilder
					.append(getPrefix(prefix, count == (items.size() - 1), root, false) + renderer.apply(item) + "\n");

			tree(treeBuilder, extractor.apply(item), extractor, renderer,
					getPrefix(prefix, count == (items.size() - 1), root, true), false);

			count++;
		}
	}

	private static String getPrefix(final String prefix, final boolean last, final boolean root,
			final boolean transit) {
		return root ? prefix : prefix + (last ? (transit ? "    " : " └──") : (transit ? " │  " : " ├──"));
	}

	public static String decorate(final String text, final String prefix, final String title, final boolean wrap) {

		return decorate(Collections.singleton(text), prefix, title, wrap);
	}

	public static String decorate(final Iterable<String> texts, final String prefix, final String title,
			final boolean wrap) {

		StringBuilder stringBuilder = new StringBuilder();

		int max = 0;

		for (String text : texts) {

			String[] entries = text != null ? text.split("\\R") : new String[0];

			for (String entry : entries) {
				max = Math.max(max, entry.length());
			}
		}

		if (wrap) {

			if (title != null) {

				StringBuilder titleBuilder = new StringBuilder();

				titleBuilder.append("┌─");
				titleBuilder.append(title);

				if ((max - title.length()) >= 1) {
					titleBuilder.append(String.join("", Collections.nCopies(max - title.length(), "─")));
				}

				if ((max - title.length()) >= 0) {
					titleBuilder.append("─┐");
				}

				stringBuilder.append(prefix != null ? prefix : "");
				stringBuilder.append(titleBuilder.toString());
				stringBuilder.append("\n");

			} else {

				StringBuilder titleBuilder = new StringBuilder();

				titleBuilder.append("┌─");
				titleBuilder.append(String.join("", Collections.nCopies(max, "─")));
				titleBuilder.append("─┐");

				stringBuilder.append(prefix != null ? prefix : "");
				stringBuilder.append(titleBuilder.toString());
				stringBuilder.append("\n");
			}
		}

		int count = 0;

		for (String text : texts) {

			if ((count > 0) && wrap) {

				StringBuilder separatorBuilder = new StringBuilder();

				separatorBuilder.append("├─");
				separatorBuilder.append(String.join("", Collections.nCopies(max, "─")));
				separatorBuilder.append("─┤");

				stringBuilder.append(prefix != null ? prefix : "");
				stringBuilder.append(separatorBuilder.toString());
				stringBuilder.append("\n");
			}

			String[] entries = text != null ? text.split("\\R") : new String[0];

			for (String entry : entries) {

				StringBuilder bodyBuilder = new StringBuilder();

				if (wrap) {
					bodyBuilder.append("│ ");
				}

				bodyBuilder.append(entry);

				if (wrap) {

					bodyBuilder.append(String.join("", Collections.nCopies(max - entry.length(), " ")));
					bodyBuilder.append(" │");
				}

				stringBuilder.append(prefix != null ? prefix : "");
				stringBuilder.append(bodyBuilder.toString());
				stringBuilder.append("\n");
			}

			count++;
		}

		if (wrap) {

			StringBuilder footerBuilder = new StringBuilder();

			footerBuilder.append("└─");
			footerBuilder.append(String.join("", Collections.nCopies(max, "─")));
			footerBuilder.append("─┘");

			stringBuilder.append(prefix != null ? prefix : "");
			stringBuilder.append(footerBuilder.toString());
			stringBuilder.append("\n");
		}

		return stringBuilder.toString();
	}

	public static <E> String table(final List<E> objects, final List<Column<E>> columns, final String prefix,
			final Border border, final boolean header) {

		Map<Column<E>, Integer> widths = columns.stream()
				.collect(
						Collectors
								.toMap(item -> item,
										item -> Integer
												.valueOf(Math.min(
														Math.max(Stream
																.concat(Stream.of(header ? item.getName() : null),
																		objects.stream()
																				.map(object -> item.getExtractor()
																						.apply(object)))
																.mapToInt(value -> value != null ? value.length() : 0)
																.max().orElse(0), item.getMinWidth()),
														item.getMaxWidth() == 0 ? Integer.MAX_VALUE
																: item.getMaxWidth()))));

		StringBuilder tableBuilder = new StringBuilder();

		if ((border == Border.ALL) || (border == Border.OUTER)) {

			StringBuilder borderBuilder = new StringBuilder();

			for (Column<E> column : columns) {

				int width = widths.get(column).intValue();

				if (borderBuilder.length() > 0) {

					if (border == Border.ALL) {
						borderBuilder.append("─┬─");
					} else if (border == Border.OUTER) {
						borderBuilder.append("─");
					}
				}

				borderBuilder.append(String.join("", Collections.nCopies(width, "─")));

			}

			if (prefix != null) {
				tableBuilder.append(prefix);
			}

			tableBuilder.append("┌─");
			tableBuilder.append(borderBuilder.toString());
			tableBuilder.append("─┐");
			tableBuilder.append("\n");
		}

		if (header) {

			StringBuilder headerBuilder = new StringBuilder();

			for (Column<E> column : columns) {

				String name = column.getName();

				int width = widths.get(column).intValue();

				if ((name != null) && (name.length() > width)) {
					name = name.substring(0, width);
				}

				if (headerBuilder.length() > 0) {

					if ((border == Border.ALL) || (border == Border.INNER)) {
						headerBuilder.append(" │ ");
					} else {
						headerBuilder.append(" ");
					}
				}

				if (name != null) {

					headerBuilder.append(String.join("", Collections.nCopies((width - name.length()) / 2, " ")));
					headerBuilder.append(name);
					headerBuilder.append(String.join("", Collections.nCopies(((width + 1) - name.length()) / 2, " ")));

				} else {

					headerBuilder.append(String.join("", Collections.nCopies(width, " ")));
				}
			}

			if (prefix != null) {
				tableBuilder.append(prefix);
			}

			if ((border == Border.ALL) || (border == Border.OUTER)) {
				tableBuilder.append("│ ");
			}

			tableBuilder.append(headerBuilder.toString());

			if ((border == Border.ALL) || (border == Border.OUTER)) {
				tableBuilder.append(" │");
			}

			tableBuilder.append("\n");
		}

		boolean separator = header;

		for (E object : objects) {

			if (separator) {

				if ((border == Border.ALL) || (border == Border.INNER)) {

					StringBuilder borderBuilder = new StringBuilder();

					for (Column<E> column : columns) {

						int width = widths.get(column).intValue();

						if (borderBuilder.length() > 0) {
							borderBuilder.append("─┼─");
						}

						borderBuilder.append(String.join("", Collections.nCopies(width, "─")));
					}

					if (prefix != null) {
						tableBuilder.append(prefix);
					}

					if (border == Border.ALL) {
						tableBuilder.append("├─");
					}

					tableBuilder.append(borderBuilder.toString());

					if (border == Border.ALL) {
						tableBuilder.append("─┤");
					}

					tableBuilder.append("\n");
				}
			}

			if (border == Border.ALL) {
				separator = true;
			} else if (border == Border.INNER) {
				separator = false;
			}

			StringBuilder rowBuilder = new StringBuilder();

			for (Column<E> column : columns) {

				String value = column.getExtractor().apply(object);
				Align align = column.getAlign();

				int width = widths.get(column).intValue();

				if ((value != null) && (value.length() > width)) {
					value = value.substring(0, width);
				}

				if (rowBuilder.length() > 0) {

					if ((border == Border.ALL) || (border == Border.INNER)) {
						rowBuilder.append(" │ ");
					} else {
						rowBuilder.append(" ");
					}
				}

				if (value != null) {

					if (align == Align.RIGHT) {

						rowBuilder.append(String.join("", Collections.nCopies(width - value.length(), " ")));
						rowBuilder.append(value);

					} else if (align == Align.CENTER) {

						rowBuilder.append(String.join("", Collections.nCopies((width - value.length()) / 2, " ")));
						rowBuilder.append(value);
						rowBuilder
								.append(String.join("", Collections.nCopies(((width + 1) - value.length()) / 2, " ")));

					} else {

						rowBuilder.append(value);
						rowBuilder.append(String.join("", Collections.nCopies(width - value.length(), " ")));
					}

				} else {

					rowBuilder.append(String.join("", Collections.nCopies(width, " ")));
				}
			}

			if (prefix != null) {
				tableBuilder.append(prefix);
			}

			if ((border == Border.ALL) || (border == Border.OUTER)) {
				tableBuilder.append("│ ");
			}

			tableBuilder.append(rowBuilder.toString());

			if ((border == Border.ALL) || (border == Border.OUTER)) {
				tableBuilder.append(" │");
			}

			tableBuilder.append("\n");
		}

		if ((border == Border.ALL) || (border == Border.OUTER)) {

			StringBuilder borderBuilder = new StringBuilder();

			for (Column<E> column : columns) {

				int width = widths.get(column).intValue();

				if (borderBuilder.length() > 0) {

					if (border == Border.ALL) {
						borderBuilder.append("─┴─");
					} else if (border == Border.OUTER) {
						borderBuilder.append("─");
					}
				}

				borderBuilder.append(String.join("", Collections.nCopies(width, "─")));
			}

			if (prefix != null) {
				tableBuilder.append(prefix);
			}

			tableBuilder.append("└─");
			tableBuilder.append(borderBuilder.toString());
			tableBuilder.append("─┘");
		}

		return tableBuilder.toString();
	}

	public static class Column<E> {

		private final String name;

		private final Align align;

		private final int minWidth;

		private final int maxWidth;

		private final Function<E, String> extractor;

		private Column(final String name, final Align align, final int minWidth, final int maxWidth,
				final Function<E, String> extractor) {

			this.name = name;
			this.align = align;
			this.minWidth = Math.max(minWidth, 0);
			this.maxWidth = Math.max(maxWidth, 0);
			this.extractor = extractor;
		}

		public String getName() {
			return name;
		}

		public Align getAlign() {
			return align;
		}

		public int getMinWidth() {
			return minWidth;
		}

		public int getMaxWidth() {
			return maxWidth;
		}

		public Function<E, String> getExtractor() {
			return extractor;
		}

		public static <E> ColumnBuilder<E> builder() {
			return new ColumnBuilder<>();
		}
	}

	public static class ColumnBuilder<E> {

		private String name;

		private Align align;

		private int minWidth;

		private int maxWidth;

		private Function<E, String> extractor;

		private ColumnBuilder() {
			// Empty
		}

		public ColumnBuilder<E> name(final String name) {

			this.name = name;
			return this;
		}

		public ColumnBuilder<E> align(final Align align) {

			this.align = align;
			return this;
		}

		public ColumnBuilder<E> minWidth(final int minWidth) {

			this.minWidth = minWidth;
			return this;
		}

		public ColumnBuilder<E> maxWidth(final int maxWidth) {

			this.maxWidth = maxWidth;
			return this;
		}

		public ColumnBuilder<E> extractor(final Function<E, String> extractor) {

			this.extractor = extractor;
			return this;
		}

		public Column<E> build() {
			return new Column<>(name, align, minWidth, maxWidth, extractor);
		}
	}

	public static enum Align {

		LEFT, RIGHT, CENTER;
	}

	public static enum Border {

		ALL, OUTER, INNER, NONE;
	}
}
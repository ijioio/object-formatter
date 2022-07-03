package com.ijioio.object.format.test.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.ijioio.object.format.ObjectFormatter;
import com.ijioio.object.format.annotation.FormatElement;
import com.ijioio.object.format.annotation.FormatType;
import com.ijioio.object.format.converter.Converter;
import com.ijioio.object.format.converter.ConverterRegistry;
import com.ijioio.object.format.test.builder.BookingBuilder;
import com.ijioio.object.format.test.builder.BookingBuilder.BookingPrototypeAdapter;
import com.ijioio.object.format.test.builder.SegmentBuilder.SegmentPrototypeAdapter;
import com.ijioio.object.format.test.builder.model.Passenger;

public class ObjectFormatterConverterCollectionElementTest {

	@Test
	public void converterCollectionSimpleTest() {

		ConverterRegistry.get().register(Segment.class, new SegmentDefaultConverter());

		Booking booking = BookingBuilder.of(Booking.class) //
				.number("1234567890") //
				.segment(Segment.class) //
				.pnr("B92X4Q") //
				.end() //
				.segment(Segment.class) //
				.pnr("N38R5E") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${id=number} ${id=segments}");

		String expected = "1234567890 [EC] B92X4Q, [EC] N38R5E";
		String actual = format.format(booking, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	public static class SegmentDefaultConverter implements Converter<Segment, String> {

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		public String convert(Segment value) throws Exception {
			return "[DC] " + value.getPnr();
		}
	}

	public static class SegmentTypeConverter implements Converter<Segment, String> {

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		public String convert(Segment value) throws Exception {
			return "[TC] " + value.getPnr();
		}
	}

	public static class SegmentElementConverter implements Converter<Collection<Segment>, String> {

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		public String convert(Collection<Segment> value) throws Exception {
			return value.stream().map(item -> "[EC] " + item.getPnr()).collect(Collectors.joining(", "));
		}
	}

	public static class Booking extends BookingPrototypeAdapter<Passenger, Segment> {

		private String number;

		@FormatElement(converter = SegmentElementConverter.class)
		private List<Segment> segments = new ArrayList<>();

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public void setNumber(String number) {
			this.number = number;
		}

		@Override
		public Collection<Segment> getSegments() {
			return segments;
		}

		@Override
		public String toString() {
			return "[ST] " + number;
		}
	}

	@FormatType(converter = SegmentTypeConverter.class)
	public static class Segment extends SegmentPrototypeAdapter {

		private String pnr;

		@Override
		public String getPnr() {
			return pnr;
		}

		@Override
		public void setPnr(String pnr) {
			this.pnr = pnr;
		}

		@Override
		public String toString() {
			return "[ST] " + pnr;
		}
	}
}

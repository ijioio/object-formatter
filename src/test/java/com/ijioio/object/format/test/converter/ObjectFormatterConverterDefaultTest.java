package com.ijioio.object.format.test.converter;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.ijioio.object.format.ObjectFormatter;
import com.ijioio.object.format.converter.Converter;
import com.ijioio.object.format.converter.ConverterRegistry;
import com.ijioio.object.format.test.builder.BookingBuilder;
import com.ijioio.object.format.test.builder.BookingBuilder.BookingPrototypeAdapter;
import com.ijioio.object.format.test.builder.PassengerBuilder.PassengerPrototypeAdapter;

public class ObjectFormatterConverterDefaultTest {

	@Test
	public void converterDefaultTest() {

		ConverterRegistry.get().register(Passenger.class, new PassengerDefaultConverter());

		Booking booking = BookingBuilder.of(Booking.class) //
				.number("1234567890") //
				.passenger(Passenger.class) //
				.firstName("Donald") //
				.lastName("Heathfield") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${id=number} ${id=passenger}");

		String expected = "1234567890 [DC] Donald Heathfield";
		String actual = format.format(booking, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	public static class PassengerDefaultConverter implements Converter<Passenger, String> {

		@Override
		public Class<String> getType() {
			return String.class;
		}

		@Override
		public String convert(Passenger value) throws Exception {
			return "[DC] " + value.getFirstName() + " " + value.getLastName();
		}
	}

	public static class Booking extends BookingPrototypeAdapter<Passenger> {

		private String number;

		private Passenger passenger;

		@Override
		public String getNumber() {
			return number;
		}

		@Override
		public void setNumber(String number) {
			this.number = number;
		}

		@Override
		public Passenger getPassenger() {
			return passenger;
		}

		@Override
		public void setPassenger(Passenger passenger) {
			this.passenger = passenger;
		}

		@Override
		public String toString() {
			return "[ST] " + number;
		}
	}

	public static class Passenger extends PassengerPrototypeAdapter {

		private String firstName;

		private String lastName;

		@Override
		public String getFirstName() {
			return firstName;
		}

		@Override
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		@Override
		public String getLastName() {
			return lastName;
		}

		@Override
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		@Override
		public String toString() {
			return "[ST] " + firstName + " " + lastName;
		}
	}
}

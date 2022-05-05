package com.ijioio.object.format.test;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.ObjectFormatter;
import com.ijioio.object.format.annotation.FormatType;
import com.ijioio.object.format.formatter.Formatter;
import com.ijioio.object.format.formatter.FormatterRegistry;
import com.ijioio.object.format.object.ObjectHolder;
import com.ijioio.object.format.util.PatternUtil.Pattern;

public class ObjectFormatterFormatterSelfCollectionTest {

	@Before
	public void init() {

		FormatterRegistry.get().register(Passport.class, new PassportDefaultFormatter());
		FormatterRegistry.get().register(Job.class, new JobDefaultFormatter());
	}

	@Test
	public void formatterTest() {

		Person person = Person.builder() //
				.name("Thomas Ritton") //
				.build();

		Passport passport = Passport.builder() //
				.number("1234567890") //
				.build();

		Job job = Job.builder() //
				.position("Manager") //
				.build();

		ObjectFormatter format = ObjectFormatter.of(Pattern.self());

		String expected = "[ST] Thomas Ritton";
		String actual = format.format(Collections.singletonList(person), Locale.ENGLISH);

		Assert.assertEquals(expected, actual);

		expected = "[DF] 1234567890";
		actual = format.format(Collections.singletonList(passport), Locale.ENGLISH);

		Assert.assertEquals(expected, actual);

		expected = "[TF] Manager";
		actual = format.format(Collections.singletonList(job), Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	public static class Person {

		public static PersonBuilder builder() {
			return new PersonBuilder();
		}

		private final String name;

		private Person(PersonBuilder builder) {

			this.name = builder.name;
		}

		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "[ST] " + name;
		}
	}

	public static class PersonBuilder {

		private String name;

		private PersonBuilder() {
			// Empty
		}

		public PersonBuilder name(String name) {

			this.name = name;
			return this;
		}

		public Person build() {
			return new Person(this);
		}
	}

	public static class Passport {

		public static PassportBuilder builder() {
			return new PassportBuilder();
		}

		private final String number;

		private Passport(PassportBuilder builder) {

			this.number = builder.number;
		}

		public String getNumber() {
			return number;
		}

		@Override
		public String toString() {
			return "[ST] " + number;
		}
	}

	public static class PassportDefaultFormatter implements Formatter<Passport> {

		@Override
		public String format(ObjectHolder<Passport> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[DF] " + item.getNumber()).orElse(null);
		}
	}

	public static class PassportTypeFormatter implements Formatter<Passport> {

		@Override
		public String format(ObjectHolder<Passport> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[TF] " + item.getNumber()).orElse(null);
		}
	}

	public static class PassportElementFormatter implements Formatter<Passport> {

		@Override
		public String format(ObjectHolder<Passport> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[EF] " + item.getNumber()).orElse(null);
		}
	}

	public static class PassportBuilder {

		private String number;

		private PassportBuilder() {
			// Empty
		}

		public PassportBuilder number(String number) {

			this.number = number;
			return this;
		}

		public Passport build() {
			return new Passport(this);
		}
	}

	@FormatType(formatter = JobTypeFormatter.class)
	public static class Job {

		public static JobBuilder builder() {
			return new JobBuilder();
		}

		private final String position;

		private Job(JobBuilder builder) {

			this.position = builder.position;
		}

		public String getPosition() {
			return position;
		}

		@Override
		public String toString() {
			return "[ST] " + position;
		}
	}

	public static class JobDefaultFormatter implements Formatter<Job> {

		@Override
		public String format(ObjectHolder<Job> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[DF] " + item.getPosition()).orElse(null);
		}
	}

	public static class JobTypeFormatter implements Formatter<Job> {

		@Override
		public String format(ObjectHolder<Job> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[TF] " + item.getPosition()).orElse(null);
		}
	}

	public static class JobElementFormatter implements Formatter<Job> {

		@Override
		public String format(ObjectHolder<Job> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[EF] " + item.getPosition()).orElse(null);
		}
	}

	public static class JobBuilder {

		private String position;

		private JobBuilder() {
			// Empty
		}

		public JobBuilder position(String position) {

			this.position = position;
			return this;
		}

		public Job build() {
			return new Job(this);
		}
	}
}

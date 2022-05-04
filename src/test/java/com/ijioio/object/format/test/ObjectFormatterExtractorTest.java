package com.ijioio.object.format.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.ijioio.object.format.ObjectFormatter;
import com.ijioio.object.format.annotation.FormatElement;
import com.ijioio.object.format.extractor.Extractor;

public class ObjectFormatterExtractorTest {

	@Test
	public void extractorTest() {

		Person person = Person.builder() //
				.name("Thomas Ritton") //
				.passport() //
				.number("1234567890") //
				.end() //
				.job() //
				.position("Manager") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${id=name} ${id=passports} ${id=jobs}");

		String expected = "Thomas Ritton [ST] 1234567890 [ST] Accountant";
		String actual = format.format(person, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	public static class Person {

		public static PersonBuilder builder() {
			return new PersonBuilder();
		}

		private final String name;

		private final List<Passport> passports = new ArrayList<>();

		@FormatElement(extractor = JobElementExtractor.class)
		private final List<Job> jobs = new ArrayList<>();

		private Person(PersonBuilder builder) {

			this.name = builder.name;

			this.passports.clear();
			this.passports.addAll(builder.passports);

			this.jobs.clear();
			this.jobs.addAll(builder.jobs);
		}

		public String getName() {
			return name;
		}

		public List<Passport> getPassports() {
			return passports;
		}

		public List<Job> getJobs() {
			return jobs;
		}

		@Override
		public String toString() {
			return "[ST] " + name;
		}
	}

	public static class PersonBuilder {

		private String name;

		private List<Passport> passports = new ArrayList<>();

		private List<Job> jobs = new ArrayList<>();

		private PersonBuilder() {
			// Empty
		}

		public PersonBuilder name(String name) {

			this.name = name;
			return this;
		}

		public PersonBuilder passport(Passport passport) {

			this.passports.add(passport);
			return this;
		}

		public PassportBuilder passport() {
			return Passport.builder(this);
		}

		public PersonBuilder job(Job job) {

			this.jobs.add(job);
			return this;
		}

		public JobBuilder job() {
			return Job.builder(this);
		}

		public Person build() {
			return new Person(this);
		}
	}

	public static class Passport {

		public static PassportBuilder builder() {
			return new PassportBuilder(null);
		}

		public static PassportBuilder builder(PersonBuilder parent) {
			return new PassportBuilder(parent);
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

	public static class PassportElementExtractor implements Extractor<Person> {

		@Override
		public Object extract(Person person) throws Exception {
			return Collections.singletonList(Passport.builder().number("0987654321").build());
		}
	}

	public static class PassportBuilder {

		private final PersonBuilder parent;

		private String number;

		private PassportBuilder(PersonBuilder parent) {
			this.parent = parent;
		}

		public PassportBuilder number(String number) {

			this.number = number;
			return this;
		}

		public PersonBuilder end() {

			parent.passport(build());
			return parent;
		}

		public Passport build() {
			return new Passport(this);
		}
	}

	public static class Job {

		public static JobBuilder builder() {
			return new JobBuilder(null);
		}

		public static JobBuilder builder(PersonBuilder parent) {
			return new JobBuilder(parent);
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

	public static class JobElementExtractor implements Extractor<Person> {

		@Override
		public Object extract(Person person) throws Exception {
			return Collections.singletonList(Job.builder().position("Accountant").build());
		}
	}

	public static class JobBuilder {

		private final PersonBuilder parent;

		private String position;

		private JobBuilder(PersonBuilder parent) {
			this.parent = parent;
		}

		public JobBuilder position(String position) {

			this.position = position;
			return this;
		}

		public PersonBuilder end() {

			parent.job(build());
			return parent;
		}

		public Job build() {
			return new Job(this);
		}
	}
}

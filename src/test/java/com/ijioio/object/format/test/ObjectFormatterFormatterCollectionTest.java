package com.ijioio.object.format.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.ObjectFormatter;
import com.ijioio.object.format.annotation.FormatElement;
import com.ijioio.object.format.annotation.FormatType;
import com.ijioio.object.format.formatter.Formatter;
import com.ijioio.object.format.formatter.FormatterRegistry;
import com.ijioio.object.format.object.ObjectHolder;
import com.ijioio.object.format.util.PatternUtil.Pattern;

public class ObjectFormatterFormatterCollectionTest {

	@Before
	public void init() {

		FormatterRegistry.get().register(Job.class, new JobDefaultFormatter());
		FormatterRegistry.get().register(Hobby.class, new HobbyDefaultFormatter());
		FormatterRegistry.get().register(Pet.class, new PetDefaultFormatter());
	}

	@Test
	public void formatterCollectionTest() {

		Person person = Person.builder() //
				.name("Thomas Ritton") //
				.passport() //
				.number("1234567890") //
				.end() //
				.job() //
				.position("Manager") //
				.end() //
				.hobby() //
				.name("Driving") //
				.end() //
				.pet() //
				.name("Charlie") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${id=name} ${id=passports} ${id=jobs} ${id=hobbies} ${id=pets}");

		String expected = "Thomas Ritton [ST] 1234567890 [DF] Manager [TF] Driving [EF] Charlie";
		String actual = format.format(person, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	public static class Person {

		public static PersonBuilder builder() {
			return new PersonBuilder();
		}

		private final String name;

		private final List<Passport> passports = new ArrayList<>();

		private final List<Job> jobs = new ArrayList<>();

		private final List<Hobby> hobbies = new ArrayList<>();

		@FormatElement(formatter = PetElementFormatter.class)
		private final List<Pet> pets = new ArrayList<>();

		private Person(PersonBuilder builder) {

			this.name = builder.name;

			this.passports.clear();
			this.passports.addAll(builder.passports);

			this.jobs.clear();
			this.jobs.addAll(builder.jobs);

			this.hobbies.clear();
			this.hobbies.addAll(builder.hobbies);

			this.pets.clear();
			this.pets.addAll(builder.pets);
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

		public List<Hobby> getHobbies() {
			return hobbies;
		}

		public List<Pet> getPets() {
			return pets;
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

		private List<Hobby> hobbies = new ArrayList<>();

		private List<Pet> pets = new ArrayList<>();

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

		public PersonBuilder hobby(Hobby hobby) {

			this.hobbies.add(hobby);
			return this;
		}

		public HobbyBuilder hobby() {
			return Hobby.builder(this);
		}

		public PersonBuilder pet(Pet pet) {

			this.pets.add(pet);
			return this;
		}

		public PetBuilder pet() {
			return Pet.builder(this);
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

	public static class PassportElementFormatter implements Formatter<List<Passport>> {

		@Override
		public String format(ObjectHolder<List<Passport>> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return objectHolder.getObject().stream().map(item -> "[EF] " + item.getNumber())
					.collect(Collectors.joining(Pattern.SEPARATOR));
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

	public static class JobElementFormatter implements Formatter<List<Job>> {

		@Override
		public String format(ObjectHolder<List<Job>> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return objectHolder.getObject().stream().map(item -> "[EF] " + item.getPosition())
					.collect(Collectors.joining(Pattern.SEPARATOR));
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

	@FormatType(formatter = HobbyTypeFormatter.class)
	public static class Hobby {

		public static HobbyBuilder builder() {
			return new HobbyBuilder(null);
		}

		public static HobbyBuilder builder(PersonBuilder parent) {
			return new HobbyBuilder(parent);
		}

		private final String name;

		private Hobby(HobbyBuilder builder) {

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

	public static class HobbyDefaultFormatter implements Formatter<Hobby> {

		@Override
		public String format(ObjectHolder<Hobby> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[DF] " + item.getName()).orElse(null);
		}
	}

	public static class HobbyTypeFormatter implements Formatter<Hobby> {

		@Override
		public String format(ObjectHolder<Hobby> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[TF] " + item.getName()).orElse(null);
		}
	}

	public static class HobbyElementFormatter implements Formatter<List<Hobby>> {

		@Override
		public String format(ObjectHolder<List<Hobby>> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return objectHolder.getObject().stream().map(item -> "[EF] " + item.getName())
					.collect(Collectors.joining(Pattern.SEPARATOR));
		}
	}

	public static class HobbyBuilder {

		private final PersonBuilder parent;

		private String name;

		private HobbyBuilder(PersonBuilder parent) {
			this.parent = parent;
		}

		public HobbyBuilder name(String name) {

			this.name = name;
			return this;
		}

		public PersonBuilder end() {

			parent.hobby(build());
			return parent;
		}

		public Hobby build() {
			return new Hobby(this);
		}
	}

	@FormatType(formatter = PetTypeFormatter.class)
	public static class Pet {

		public static PetBuilder builder() {
			return new PetBuilder(null);
		}

		public static PetBuilder builder(PersonBuilder parent) {
			return new PetBuilder(parent);
		}

		private final String name;

		private Pet(PetBuilder builder) {

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

	public static class PetDefaultFormatter implements Formatter<Pet> {

		@Override
		public String format(ObjectHolder<Pet> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[DF] " + item.getName()).orElse(null);
		}
	}

	public static class PetTypeFormatter implements Formatter<Pet> {

		@Override
		public String format(ObjectHolder<Pet> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return Optional.ofNullable(objectHolder.getObject()).map(item -> "[TF] " + item.getName()).orElse(null);
		}
	}

	public static class PetElementFormatter implements Formatter<List<Pet>> {

		@Override
		public String format(ObjectHolder<List<Pet>> objectHolder, Configuration configuration, String separator,
				String pattern, Locale locale) throws Exception {
			return objectHolder.getObject().stream().map(item -> "[EF] " + item.getName())
					.collect(Collectors.joining(Pattern.SEPARATOR));
		}
	}

	public static class PetBuilder {

		private final PersonBuilder parent;

		private String name;

		private PetBuilder(PersonBuilder parent) {
			this.parent = parent;
		}

		public PetBuilder name(String name) {

			this.name = name;
			return this;
		}

		public PersonBuilder end() {

			parent.pet(build());
			return parent;
		}

		public Pet build() {
			return new Pet(this);
		}
	}
}

package com.ijioio.object.format.test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.ijioio.object.format.Configuration;
import com.ijioio.object.format.ObjectFormatter;

public class ObjectFormatterTest {

	@Test
	public void formatSimple() {

		F1Driver driver = F1Driver.builder().build();

		ObjectFormatter format = ObjectFormatter.of("simple");

		String expected = "simple";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void formatVariableOfStringBasic() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.middleName("Carl Davidson") //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${firstName} ${middleName} ${lastName}");

		String expected = "Lewis Carl Davidson Hamilton";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void formatVariableOfStringCanonical() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.middleName("Carl Davidson") //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${id=firstName} ${id=middleName} ${id=lastName}");

		String expected = "Lewis Carl Davidson Hamilton";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void formatVariableOfStringBasicAndCanonical() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.middleName("Carl Davidson") //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${firstName} ${middleName} ${id=lastName}");

		String expected = "Lewis Carl Davidson Hamilton";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void formatVariableOfStringAndDefault() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName(null) //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${id=firstName} ${id=lastName|default=Anonymous}");

		String expected = "Lewis Anonymous";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void formatVariableOfStringAndDate() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.middleName("Carl Davidson") //
				.birthdate(LocalDate.of(1985, Month.JANUARY, 7)) //
				.build();

		ObjectFormatter format = ObjectFormatter.of("${id=firstName} ${id=lastName} ${id=birthdate}");

		String expected = "Lewis Hamilton 1985-01-07";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void formatVariableOfStringAndDateWithPattern() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.middleName("Carl Davidson") //
				.birthdate(LocalDate.of(1985, Month.JANUARY, 7)) //
				.build();

		ObjectFormatter format = ObjectFormatter
				.of("${id=firstName} ${id=lastName} ${id=birthdate|pattern=yyyy.MM.dd}");

		String expected = "Lewis Hamilton 1985.01.07";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void parserConfigurationCustomEscapeSequence() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.build();

		Configuration configuration = Configuration.builder() //
				.parserConfiguration() //
				.escapeSequence("##") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter.of(
				"##Name: ${id=firstName} ${id=lastName}## #### ${id=##first##Name} ${##id##=lastName}", configuration);

		String expected = "Name: ${id=firstName} ${id=lastName} ## Lewis Hamilton";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void parserConfigurationCustomVariableDelimiterSequence() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.build();

		Configuration configuration = Configuration.builder() //
				.parserConfiguration() //
				.variableStartSequence("<<") //
				.variableEndSequence(">>") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter.of("Name: <<id=firstName>> <<id=lastName>>", configuration);

		String expected = "Name: Lewis Hamilton";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void parserConfigurationCustomVariableSeparatorSequence() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName(null) //
				.build();

		Configuration configuration = Configuration.builder() //
				.parserConfiguration() //
				.variableSeparatorSequence("!!") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter
				.of("Name: ${id=firstName!!default=Anonymous} ${id=lastName!!default=Anonymous}", configuration);

		String expected = "Name: Lewis Anonymous";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void parserConfigurationCustomVariablePropertySeparatorSequence() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.build();

		Configuration configuration = Configuration.builder() //
				.parserConfiguration() //
				.variablePropertySeparatorSequence("->") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter.of("Name: ${id->firstName} ${id->lastName}", configuration);

		String expected = "Name: Lewis Hamilton";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void parserConfigurationCustomVariablePropertyNameIdSequence() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.build();

		Configuration configuration = Configuration.builder() //
				.parserConfiguration() //
				.variablePropertyNameIdSequence("idproperty") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter.of("Name: ${idproperty=firstName} ${idproperty=lastName}",
				configuration);

		String expected = "Name: Lewis Hamilton";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void parserConfigurationCustomVariablePropertyValueSeparatorSequence() {

		F1Driver driver = F1Driver.builder() //
				.firstName("Lewis") //
				.lastName("Hamilton") //
				.build();

		Configuration configuration = Configuration.builder() //
				.parserConfiguration() //
				.variablePropertyValueSeparatorSequence("::") //
				.end() //
				.build();

		ObjectFormatter format = ObjectFormatter
				.of("Name: ${id=com.ijioio.object.format.test.ObjectFormatterTest$F1Driver::firstName} "
						+ "${id=com.ijioio.object.format.test.ObjectFormatterTest$F1Driver::lastName}", configuration);

		String expected = "Name: Lewis Hamilton";
		String actual = format.format(driver, Locale.ENGLISH);

		Assert.assertEquals(expected, actual);
	}

	static class F1Driver {

		public static F1DriverBuilder builder() {
			return new F1DriverBuilder();
		}

		private final String firstName;

		private final String lastName;

		private final String middleName;

		private LocalDate birthdate;

		private F1Driver(F1DriverBuilder builder) {

			this.firstName = builder.firstName;
			this.lastName = builder.lastName;
			this.middleName = builder.middleName;
			this.birthdate = builder.birthdate;
		}

		public LocalDate getBirthdate() {
			return birthdate;
		}

		public void setBirthdate(LocalDate birthdate) {
			this.birthdate = birthdate;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getMiddleName() {
			return middleName;
		}

		@Override
		public String toString() {
			return "F1Driver [firstName=" + firstName + ", lastName=" + lastName + ", middleName=" + middleName
					+ ", birthdate=" + birthdate + "]";
		}
	}

	static class F1DriverBuilder {

		private String firstName;

		private String lastName;

		private String middleName;

		private LocalDate birthdate;

		private F1DriverBuilder() {
			// Empty
		}

		public F1DriverBuilder firstName(String firstName) {

			this.firstName = firstName;
			return this;
		}

		public F1DriverBuilder lastName(String lastName) {

			this.lastName = lastName;
			return this;
		}

		public F1DriverBuilder middleName(String middleName) {

			this.middleName = middleName;
			return this;
		}

		public F1DriverBuilder birthdate(LocalDate birthdate) {

			this.birthdate = birthdate;
			return this;
		}

		public F1Driver build() {
			return new F1Driver(this);
		}
	}
}

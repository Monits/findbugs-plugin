package com.monits.findbugs.jdk;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class UselessValueOfTest extends BaseDetectorTest {

	private EasyBugReporter reporter;

	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testUselessStringValueOfString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessStringValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessStringValueOfCall")
				.inMethod("getStringValueOfString")
				.build()
		);
	}

	@Test
	public void testUselessStringValueOfDummyString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessStringValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessStringValueOfCall")
				.inMethod("getStringValueOfDummyString")
				.build()
		);
	}

	@Test
	public void testStringValueOfBoxedInteger() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessStringValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessStringValueOfCall")
				.inMethod("getStringValueOfBoxedInteger")
				.build()
		);
	}

	@Test
	public void testUselessIntegerValueOfPrimitiveInteger() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessIntegerValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessIntegerValueOfCall")
				.inMethod("getIntegerValueOfPrimitiveInteger")
				.build()
		);
	}

	@Test
	public void testUselessDoubleValueOfPrimitiveDouble() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessDoubleValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessDoubleValueOfCall")
				.inMethod("getDoubleValueOfPrimitiveDouble")
				.build()
		);
	}

	@Test
	public void testDoubleValueOfString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessDoubleValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessDoubleValueOfCall")
				.inMethod("getDoubleValueOfString")
				.build()
		);
	}

	@Test
	public void testUselessLongValueOfPrimitiveLong() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessLongValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessLongValueOfCall")
				.inMethod("getLongValueOfPrimitiveLong")
				.build()
		);
	}

	@Test
	public void testLongValueOfString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessLongValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessLongValueOfCall")
				.inMethod("getLongValueOfString")
				.build()
		);
	}

	@Test
	public void testUselessFloatValueOfPrimitiveFloat() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessFloatValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessFloatValueOfCall")
				.inMethod("getFloatValueOfPrimitiveFloat")
				.build()
		);
	}

	@Test
	public void testFloatValueOfString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessFloatValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessFloatValueOfCall")
				.inMethod("getFloatValueOfString")
				.build()
		);
	}

	@Test
	public void testUselessByteValueOfPrimitiveByte() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessByteValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessByteValueOfCall")
				.inMethod("getByteValueOfPrimitiveByte")
				.build()
		);
	}

	@Test
	public void testUselessByteValueOfPrimitiveByteWithLeftShift() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessByteValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessByteValueOfCall")
				.inMethod("getByteValueOfPrimitiveByteWithLeftShift")
				.build()
		);
	}

	@Test
	public void testUselessByteValueOfPrimitiveCharacterAsByte() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessByteValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessByteValueOfCall")
				.inMethod("getByteValueOfPrimitiveCharacterAsByte")
				.build()
		);
	}

	@Test
	public void testUselessByteValueOfPrimitiveIntegerAsByte() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessByteValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessByteValueOfCall")
				.inMethod("getByteValueOfPrimitiveIntegerAsByte")
				.build()
		);
	}

	@Test
	public void testByteValueOfString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessByteValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessByteValueOfCall")
				.inMethod("getByteValueOfString")
				.build()
		);
	}

	@Test
	public void testUselessShortValueOfPrimitiveShort() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessShortValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessShortValueOfCall")
				.inMethod("getShortValueOfPrimitiveShort")
				.build()
		);
	}

	@Test
	public void testUselessShortValueOfPrimitiveShortWithLeftShift() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessShortValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessShortValueOfCall")
				.inMethod("getShortValueOfPrimitiveShortWithLeftShift")
				.build()
		);
	}

	@Test
	public void testUselessShortValueOfPrimitiveCharacterAsShort() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessShortValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessShortValueOfCall")
				.inMethod("getShortValueOfPrimitiveCharacterAsShort")
				.build()
		);
	}

	@Test
	public void testUselessShortValueOfPrimitiveIntegerAsShort() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessShortValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessShortValueOfCall")
				.inMethod("getShortValueOfPrimitiveIntegerAsShort")
				.build()
		);
	}

	@Test
	public void testShortValueOfString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessShortValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessShortValueOfCall")
				.inMethod("getShortValueOfString")
				.build()
		);
	}

	@Test
	public void testUselessCharacterValueOfPrimitiveCharacter() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessCharacterValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessCharacterValueOfCall")
				.inMethod("getCharacterValueOfPrimitiveCharacter")
				.build()
		);
	}

	@Test
	public void testUselessCharacterValueOfPrimitiveCharacterWithLeftShift() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessCharacterValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessCharacterValueOfCall")
				.inMethod("getCharacterValueOfPrimitiveCharacterWithLeftShift")
				.build()
		);
	}

	@Test
	public void testUselessCharacterValueOfPrimitiveIntegerAsCharacter() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/valueof/UselessCharacterValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessValueOfCallDetector.USELESS_VALUEOF_CALL)
				.inClass("UselessCharacterValueOfCall")
				.inMethod("getCharacterValueOfPrimitiveIntegerAsCharacter")
				.build()
		);
	}
}
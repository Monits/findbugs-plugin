package com.monits.findbugs.jdk;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class UselessStringValueOfTest extends BaseDetectorTest {

	private EasyBugReporter reporter;

	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testUselessStringValueOfString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/UselessStringValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessStringValueOfCallDetector.USELESS_STRING_VALUEOF_CALL)
				.inClass("UselessStringValueOfCall")
				.inMethod("getStringValueOfString")
				.build()
		);
	}

	@Test
	public void testUselessStringValueOfDummyString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/UselessStringValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(UselessStringValueOfCallDetector.USELESS_STRING_VALUEOF_CALL)
				.inClass("UselessStringValueOfCall")
				.inMethod("getStringValueOfDummyString")
				.build()
		);
	}

	@Test
	public void getStringValueOfPrimitiveInteger() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/UselessStringValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(UselessStringValueOfCallDetector.USELESS_STRING_VALUEOF_CALL)
				.inClass("UselessStringValueOfCall")
				.inMethod("getStringValueOfPrimitiveInteger")
				.build()
		);
	}

	@Test
	public void getString() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/UselessStringValueOfCall"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(UselessStringValueOfCallDetector.USELESS_STRING_VALUEOF_CALL)
				.inClass("UselessStringValueOfCall")
				.inMethod("getString")
				.build()
		);
	}
}
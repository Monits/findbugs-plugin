package com.monits.findbugs.effectivejava;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class EqualsOverrideDetectorTest extends BaseDetectorTest {

	private static final String DONT_OVERRIDE_EQUALS = "DONT_OVERRIDE_EQUALS";
	
	private EasyBugReporter reporter;

	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testNoEqualsOverride() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item8/BaseConcreteClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(DONT_OVERRIDE_EQUALS)
				.build()
		);
	}
	
	@Test
	public void testGoodOverride() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item8/BaseNonEqualsConcreteClass"),
			getClassFilePath("samples/effectivejava/item8/GoodEqualsOverride"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(DONT_OVERRIDE_EQUALS)
				.build()
		);
	}
	
	@Test
	public void testBadOverride() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item8/BaseConcreteClass"),
			getClassFilePath("samples/effectivejava/item8/BadEqualsOverride"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(DONT_OVERRIDE_EQUALS)
				.inClass("BadEqualsOverride")
				.build()
		);
	}
}

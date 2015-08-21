package com.monits.findbugs.jdk;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class PatternCompileTest extends BaseDetectorTest {

	private EasyBugReporter reporter;

	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testReportNonStaticPatternCompile() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/NonStaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("NonStaticPatternCompile")
				.inMethod("testReportNonStaticPatternCompile")
				.build()
		);
	}

	@Test
	public void testReportNonStaticPatternCompileWithStaticRegex() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/NonStaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("NonStaticPatternCompile")
				.inMethod("testReportNonStaticPatternCompileWithStaticRegex")
				.build()
		);
	}

	@Test
	public void testNeverReportNonStaticPatternCompileWithNonFinalLocalRegex() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/NonStaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("NonStaticPatternCompile")
				.inMethod("testNeverReportNonStaticPatternCompileWithNonFinalLocalRegex")
				.build()
		);
	}

	@Test
	public void testReportNonStaticPatternCompileWithFinalLocalRegex() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/NonStaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("NonStaticPatternCompile")
				.inMethod("testReportNonStaticPatternCompileWithFinalLocalRegex")
				.build()
		);
	}

	@Test
	public void testNeverReportNonStaticPatternCompileWithFinalRegexParameter() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/NonStaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("NonStaticPatternCompile")
				.inMethod("testNeverReportNonStaticPatternCompileWithFinalRegexParameter")
				.build()
		);
	}

	@Test
	public void testNeverReportNonStaticPatternCompileWithNonFinalRegexParameter() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/NonStaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("NonStaticPatternCompile")
				.inMethod("testNeverReportNonStaticPatternCompileWithNonFinalRegexParameter")
				.build()
		);
	}

	@Test
	public void testNeverReportNonStaticPatternCompileWithRegexFromObject() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/NonStaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("NonStaticPatternCompile")
				.inMethod("testNeverReportNonStaticPatternCompileWithRegexFromObject")
				.build()
		);
	}


	@Test
	public void testMethodWithStaticPatternCompile() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/StaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("StaticPatternCompile")
				.inMethod("testStaticPatternCompile")
				.build()
		);
	}

	@Test
	public void testStaticPatternCompileInClass() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/StaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("StaticPatternCompile")
				.build()
		);
	}

	@SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
	@Test
	public void testPatternCompileWithAConcatenatedRegex() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/jdk/patterncompile/NonStaticPatternCompile"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(NonStaticPatternCompileDetector.NON_STATIC_PATTERN_COMPILE_CALL)
				.inClass("NonStaticPatternCompile")
				.inMethod("testPatternCompileWithAConcatenatedRegex")
				.build()
		);
	}
}
package com.monits.findbugs.effectivejava;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class ToStringDetectorTest extends BaseDetectorTest {

	private static final String MISSING_FIELD_IN_TO_STRING = "MISSING_FIELD_IN_TO_STRING";
	private static final String MISSING_TO_STRING_OVERRIDE = "MISSING_TO_STRING_OVERRIDE";
	
	private EasyBugReporter reporter;

	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testNoFieldClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/NoFieldClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}
	
	@Test
	public void testEnumClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/EnumWithState"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}
	
	@Test
	public void testInnerPublicStaticClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/InnerPublicStaticClass"),
			getClassFilePath("samples/effectivejava/item10/InnerPublicStaticClass$MyInnerClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}

	@Test
	public void testGoodStringClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/GoodToStringClass"),
			getClassFilePath("samples/effectivejava/item10/GoodToStringCompositeClass"),
			getClassFilePath("samples/effectivejava/item10/GoodToStringWithStaticClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}

	@Test
	public void testGoodMissingArrayClasses() throws Exception {
		// Locate test code
		final String[] files = {
			// needed in the analysis, for GoodMissingArrayToStringClass
			getClassFilePath("samples/effectivejava/item10/EnumWithState"),
			getClassFilePath("samples/effectivejava/item10/GoodMissingArrayToStringClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}
	
	@Test
	public void testStaticFieldNotReported() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/GoodToStringWithStaticClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}

	@Test
	public void testMissingToStringClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/MissingToStringClass"),
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.inClass("MissingToStringClass")
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.build()
		);
	}

	@Test
	public void testIncompleteToStringClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/IncompleteToStringClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.inClass("IncompleteToStringClass")
				.atField("name")
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}
	
	@Test
	public void testIncompleteToStringCompositeClasses() throws Exception {
		// Locate test code
		final String[] files = {
			// needed in the analysis, for BadToStringCompositeInterestingClass
			getClassFilePath("samples/effectivejava/item10/GoodToStringClass"),
			getClassFilePath("samples/effectivejava/item10/BadToStringCompositeInterestingClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.inClass("BadToStringCompositeInterestingClass")
				.atField("gtsc")
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}
	
	@Test
	public void testMissingArrayClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/ArrayMissingToStringClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.inClass("ArrayMissingToStringClass")
				.atField("rawData")
				.build()
		);
	}
	
	@Test
	public void testMissingEnumClasses() throws Exception {
		// Locate test code
		final String[] files = {
			// needed in the analysis, for BadEnumCompositeClass
			getClassFilePath("samples/effectivejava/item10/EnumWithState"),
			getClassFilePath("samples/effectivejava/item10/BadEnumCompositeClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.inClass("BadEnumCompositeClass")
				.atField("data")
				.build()
		);
	}
}

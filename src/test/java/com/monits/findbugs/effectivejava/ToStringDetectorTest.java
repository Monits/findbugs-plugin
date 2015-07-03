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
	public void testGoodStringIgnoredFieldClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/GoodToStringIgnoredFieldClass"),
			getClassFilePath("samples/effectivejava/item10/GoodToStringAllIgnoredFieldClass"),
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

	@Test
	public void testNoToStringLibraryFieldClass() throws Exception {
		// Locate test code, a class with a field of library's type with no toString() method.
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/NoToStringLibraryFieldClass"),
		};

		// Add to classPath the library with no toString() used as field type in NoToStringLibraryFieldClass Class.
		final String[] classpathes = {
			getJarFilePath("fst-1.63.jar"),
		};

		// Run the analysis
		analyze(files, classpathes, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}

	@Test
	public void testToStringLibraryFieldClass() throws Exception {
		// Locate test code, a class with a field of library's type with toString() method.
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/ToStringLibraryFieldClass"),
		};

		// Add to classPath the library with the toString() used as field type in ToStringLibraryFieldClass Class.
		final String[] classpathes = {
			getJarFilePath("fst-1.63.jar"),
		};

		// Run the analysis
		analyze(files, classpathes, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}
	
	@Test
	public void testToStringMissingLibraryFieldClass() throws Exception {
		// Locate test code, a class with a field of library's type with toString() method.
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/ToStringLibraryFieldClass"),
		};

		// Yet the jar is missing, so the class is not accessible.
		final String[] classpathes = { };

		// Run the analysis
		analyze(files, classpathes, reporter);

		// No bugs should be reported, since we are not sure
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}

	@Test
	public void testLibraryNotInClassPathFieldClass() throws Exception {
		// Locate test code, a class (not included in the classPath) with
		// a field of library's type with toString() method.
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/ToStringLibraryFieldClass"),
		};

		// This line is unnecessary, but having it make the tested case clearer.
		final String[] classpathes = {};

		// Run the analysis
		analyze(files, classpathes, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
	}
	
	@Test
	public void testCompositeOfSuppressedToString() throws Exception {
		final String[] files = {
			getClassFilePath("samples/effectivejava/item10/GoodSuppressedToStringComposite"),
			getClassFilePath("samples/effectivejava/item10/SuppressedToStringClass"),
		};

		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_TO_STRING_OVERRIDE)
				.build()
		);
		
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType(MISSING_FIELD_IN_TO_STRING)
				.build()
		);
	}
}

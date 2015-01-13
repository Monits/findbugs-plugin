package jp.co.worksap.oss.findbugs.jsr305.nullness;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class UnknownNullnessDetectorTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testPrimitive() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/PrimitiveArgument")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
	}

	@Test
	public void testAnnotatedPackage() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/annotatedpackage/package-info"),
			getClassFilePath("samples/jsr305/nullness/annotatedpackage/AnnotatedPackage")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
	}

	@Test
	public void testAnnotatedClass() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/AnnotatedClass")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
	}

	@Test
	public void testAnnotatedMethod() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/AnnotatedMethod")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
	}

	@Test
	public void testAnnotatedArgument() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/AnnotatedArgument")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
	}

	@Test
	public void testNoAnnotation() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/NoAnnotation")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.inClass("NoAnnotation")
				.build()
		);
	}

	@Test
	public void testAnnotatedReturnValue() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/AnnotatedReturnValue")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_RETURNED_VALUE")
				.build()
		);
	}

	@Test
	public void testUnannotatedReturnValue() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/UnannotatedReturnValue")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_RETURNED_VALUE")
				.inClass("UnannotatedReturnValue")
				.build()
		);
	}
}

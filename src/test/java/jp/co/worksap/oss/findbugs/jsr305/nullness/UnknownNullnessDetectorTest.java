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
	public void testAnnotatedArgumentsEnum() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/AnnotatedArgumentsEnum")
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
	public void testStandardEnum() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/StandardEnum"),
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_RETURNED_VALUE")
				.build()
		);
	}
	
	@Test
	public void testUnannotatedExtendingLibClass() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/UnannotatedExtendingLibClass"),
		};
		
		final String[] classpathes = {
			getJarFilePath("fst-1.63.jar"),
		};
		
		// Run the analysis
		analyze(files, classpathes, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
	}
	
	@Test
	public void testUnannotatedExtendingLibClassPropagatingGenerics() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/UnannotatedExtendingLibClassPropagatingGenerics"),
		};
		
		final String[] classpathes = {
			getJarFilePath("fst-1.63.jar"),
		};
		
		// Run the analysis
		analyze(files, classpathes, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
	}
	
	@Test
	public void testUnannotatedIndirectGenericBinding() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/UnannotatedIndirectGenericBinding"),
			getClassFilePath("samples/jsr305/nullness/UnannotatedIndirectGenericBinding$Bounded"),
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
	public void testUnannotatedVarargs() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/UnannotatedVarargs")
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
	public void testAnnotatedInnerClassArguments() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/AnnotatedInnerClassArguments")
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
	public void testUnannotatedInnerClassArguments() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/UnannotatedInnerClassArguments$Inner"),
			getClassFilePath("samples/jsr305/nullness/UnannotatedInnerClassArguments")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.inClass("UnannotatedInnerClassArguments$Inner")
				.build()
		);
	}
	
	@Test
	public void testAnonymousClassConstructor() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/AnonymousClassConstructor$1"),
			getClassFilePath("samples/jsr305/nullness/AnonymousClassConstructor")
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
	
	@Test
	public void testReportsOnEnumLookAlike() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/UnannotatedEnumLookAlike")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_RETURNED_VALUE")
				.inClass("UnannotatedEnumLookAlike")
				.inMethod("values")
				.build()
		);
		
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_RETURNED_VALUE")
				.inClass("UnannotatedEnumLookAlike")
				.inMethod("valueOf")
				.build()
		);
		
		verify(reporter).doReportBug(
				bugDefinition()
					.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
					.inClass("UnannotatedEnumLookAlike")
					.inMethod("valueOf")
					.build()
			);
	}
	
	@Test
	public void testUnannotatedBoundGenerics() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/nullness/UnannotatedBoundGenerics"),
			getClassFilePath("samples/jsr305/nullness/ChildOfUnannotatedBoundGenerics")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNKNOWN_NULLNESS_OF_PARAMETER")
				.build()
		);
	}
}

package jp.co.worksap.oss.findbugs.jpa;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class NullablePrimitiveDetectorTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testNullableObject() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/UseColumnDefinition"),
			getClassFilePath("samples/jpa/ColumnWithoutElement")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.build()
		);
	}

	@Test
	public void testNullablePrimitive() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/NullableBooleanColumn"),
			getClassFilePath("samples/jpa/NullableByteColumn"),
			getClassFilePath("samples/jpa/NullableShortColumn"),
			getClassFilePath("samples/jpa/NullableIntColumn"),
			getClassFilePath("samples/jpa/NullableLongColumn"),
			getClassFilePath("samples/jpa/NullableFloatColumn"),
			getClassFilePath("samples/jpa/NullableDoubleColumn"),
			getClassFilePath("samples/jpa/NullableBooleanGetter")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.inClass("NullableBooleanColumn")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.inClass("NullableByteColumn")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.inClass("NullableShortColumn")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.inClass("NullableIntColumn")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.inClass("NullableLongColumn")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.inClass("NullableFloatColumn")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.inClass("NullableDoubleColumn")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.inClass("NullableBooleanGetter")
				.build()
		);
	}

	@Test
	public void testNonNullableObject() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ColumnWithNullable")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.build()
		);
	}

	@Test
	public void testNonNullableInt() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/NonNullablePrimitiveColumn")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("NULLABLE_PRIMITIVE")
				.build()
		);
	}

}

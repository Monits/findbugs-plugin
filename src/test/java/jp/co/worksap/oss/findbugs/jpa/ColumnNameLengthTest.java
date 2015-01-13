package jp.co.worksap.oss.findbugs.jpa;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class ColumnNameLengthTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testShortName() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ShortColumnName")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("LONG_COLUMN_NAME")
				.build()
		);
	}

	@Test
	public void testShortNameWithoutAnnotationParameter() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ShortColumnNameWithoutAnnotationParameter")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("LONG_COLUMN_NAME")
				.build()
		);
	}

	@Test
	public void testLongName() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/LongColumnName")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("LONG_COLUMN_NAME")
				.inClass("LongColumnName")
				.build()
		);
	}

	@Test
	public void testLongNameWithoutAnnotationParameter() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/LongColumnNameWithoutAnnotationParameter")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("LONG_COLUMN_NAME")
				.inClass("LongColumnNameWithoutAnnotationParameter")
				.build()
		);
	}

	@Test
	public void testLongColumnNameByAnnotatedMethod() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/LongColumnNameByAnnotatedMethod")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("LONG_COLUMN_NAME")
				.inClass("LongColumnNameByAnnotatedMethod")
				.build()
		);
	}
}

package jp.co.worksap.oss.findbugs.jpa;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class ImplicitLengthTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testNegativeLength() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ColumnWithNegativeLength")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("ILLEGAL_LENGTH")
				.inClass("ColumnWithNegativeLength")
				.build()
		);
	}

	@Test
	public void testTooLongLength() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ColumnWithTooLongLength"),
			getClassFilePath("samples/jpa/GetterWithTooLongLength")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("ILLEGAL_LENGTH")
				.inClass("ColumnWithTooLongLength")
				.build()
		);
		verify(reporter).doReportBug(
		bugDefinition()
			.bugType("ILLEGAL_LENGTH")
			.inClass("GetterWithTooLongLength")
			.build()
		);
	}

	@Test
	public void testLongLengthWithLob() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ColumnWithLongLengthAndLob"),
			getClassFilePath("samples/jpa/GetterWithLongLengthAndLob")
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
	public void testExplicitLength() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ColumnWithLength")
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
	public void testImplicitLength() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ColumnWithoutElement"),
			getClassFilePath("samples/jpa/GetterWithoutElement")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("IMPLICIT_LENGTH")
				.inClass("ColumnWithoutElement")
				.build()
		);
		verify(reporter).doReportBug(
		bugDefinition()
			.bugType("IMPLICIT_LENGTH")
			.inClass("GetterWithoutElement")
			.build()
		);
	}
}

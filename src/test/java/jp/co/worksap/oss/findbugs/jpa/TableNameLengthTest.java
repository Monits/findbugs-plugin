package jp.co.worksap.oss.findbugs.jpa;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class TableNameLengthTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testShortName() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ShortTableName")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("LONG_TABLE_NAME")
				.build()
		);
	}

	@Test
	public void testShortNameWithoutAnnotationParameter() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ShortTableNameNoAnnotationPara")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("LONG_TABLE_NAME")
				.build()
		);
	}

	@Test
	public void testLongName() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/LongTableName")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("LONG_TABLE_NAME")
				.inClass("LongTableName")
				.build()
		);
	}

	@Test
	public void testLongNameWithoutAnnotationParameter() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/LongTableNameWithoutAnnotationParameter")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("LONG_TABLE_NAME")
				.inClass("LongTableNameWithoutAnnotationParameter")
				.build()
		);
	}
}

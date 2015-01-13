package jp.co.worksap.oss.findbugs.jpa;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class ColumnDefinitionTest extends BaseDetectorTest {

	private EasyBugReporter reporter;

	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testNormalClass() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ShortColumnName")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("USE_COLUMN_DEFINITION")
				.build()
		);
	}

	@Test
	public void testWithColumnDefinition() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/UseColumnDefinition")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("USE_COLUMN_DEFINITION")
				.inClass("UseColumnDefinition")
				.build()
		);
	}
}

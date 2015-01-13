package jp.co.worksap.oss.findbugs.findbugs;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class UndocumentedSuppressFBWarningsDetectorTest extends BaseDetectorTest {

	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testDocumentedClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/DocumentedSuppressFBWarnings"),
			getClassFilePath("samples/findbugs/DocumentedSuppressWarnings")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("FINDBUGS_UNDOCUMENTED_SUPPRESS_WARNINGS")
				.build()
		);
	}

	@Test
	public void testUndocumentedClasses() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/findbugs/UndocumentedSuppressFBWarnings"),
			getClassFilePath("samples/findbugs/UndocumentedSuppressWarnings")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("FINDBUGS_UNDOCUMENTED_SUPPRESS_WARNINGS")
				.inClass("UndocumentedSuppressFBWarnings")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("FINDBUGS_UNDOCUMENTED_SUPPRESS_WARNINGS")
				.inClass("UndocumentedSuppressWarnings")
				.build()
		);
	}

}

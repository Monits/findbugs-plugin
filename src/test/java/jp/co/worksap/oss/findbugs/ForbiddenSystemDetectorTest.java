package jp.co.worksap.oss.findbugs;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class ForbiddenSystemDetectorTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}
	
	@Test
	public void testUseSystemOutBug() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/system/UseSystemOut")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("FORBIDDEN_SYSTEM")
				.inClass("UseSystemOut")
				.build()
		);
	}

	@Test
	public void testUseSystemErrBug() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/system/UseSystemErr")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("FORBIDDEN_SYSTEM")
				.inClass("UseSystemErr")
				.build()
		);
	}

}

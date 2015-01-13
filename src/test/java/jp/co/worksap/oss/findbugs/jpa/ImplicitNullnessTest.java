package jp.co.worksap.oss.findbugs.jpa;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class ImplicitNullnessTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testExplicitNullness() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ColumnWithNullable")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("IMPLICIT_NULLNESS")
				.build()
		);
	}

	@Test
	public void testImplicitNullness() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ColumnWithoutElement"),
			getClassFilePath("samples/jpa/GetterWithoutElement")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("IMPLICIT_NULLNESS")
				.inClass("ColumnWithoutElement")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("IMPLICIT_NULLNESS")
				.inClass("GetterWithoutElement")
				.build()
		);
	}
}

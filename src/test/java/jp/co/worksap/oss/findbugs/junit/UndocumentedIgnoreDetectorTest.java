package jp.co.worksap.oss.findbugs.junit;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class UndocumentedIgnoreDetectorTest extends BaseDetectorTest {
	private EasyBugReporter reporter;

	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testIgnoreClassWithExplanation() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/junit/IgnoreClassWithExplanation")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNDOCUMENTED_IGNORE")
				.build()
		);
	}

	@Test
	public void testIgnoreMethodWithExplanation() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/junit/IgnoreMethodWithExplanation")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("UNDOCUMENTED_IGNORE")
				.build()
		);
	}

	@Test
	public void testIgnoreClassWithEmptyExplanation() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/junit/IgnoreClassWithEmptyExplanation")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNDOCUMENTED_IGNORE")
				.inClass("IgnoreClassWithEmptyExplanation")
				.build()
		);
	}

	@Test
	public void testIgnoreMethodWithEmptyExplanation() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/junit/IgnoreMethodWithEmptyExplanation")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNDOCUMENTED_IGNORE")
				.inClass("IgnoreMethodWithEmptyExplanation")
				.build()
		);
	}

	@Test
	public void testIgnoreClassWithoutExplanation() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/junit/IgnoreClassWithoutExplanation")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNDOCUMENTED_IGNORE")
				.inClass("IgnoreClassWithoutExplanation")
				.build()
		);
	}

	@Test
	public void testIgnoreMethodWithoutExplanation() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/junit/IgnoreMethodWithoutExplanation")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("UNDOCUMENTED_IGNORE")
				.inClass("IgnoreMethodWithoutExplanation")
				.build()
		);
	}
}

package jp.co.worksap.oss.findbugs.guava;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;


/**
 * @author tolina GmbH
 *
 */
public class UnexpectedAccessDetectorTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testNormalMethod() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/guava/ClassWhichCallsNormalMethod"),
			getClassFilePath("samples/guava/MethodWithoutVisibleForTesting")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING")
				.build()
		);
	}

	@Test
	public void testCallingAnnotatedMethod() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/guava/ClassWhichCallsVisibleMethodForTesting"),
			getClassFilePath("samples/guava/MethodWithVisibleForTesting")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING")
				.inClass("ClassWhichCallsVisibleMethodForTesting")
				.build()
		);
	}
}

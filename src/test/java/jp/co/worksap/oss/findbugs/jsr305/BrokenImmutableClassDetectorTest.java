package jp.co.worksap.oss.findbugs.jsr305;


import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class BrokenImmutableClassDetectorTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testGoodMutableClass() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/GoodMutableClass")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("IMMUTABLE_CLASS_SHOULD_BE_FINAL")
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("BROKEN_IMMUTABILITY")
				.build()
		);
	}

	@Test
	public void testEnumIsImmutable() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/ImmutableEnum")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("IMMUTABLE_CLASS_SHOULD_BE_FINAL")
				.build()
		);
		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("BROKEN_IMMUTABILITY")
				.build()
		);
	}

	@Test
	public void testBadMutableClass() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jsr305/BadImmutableClass"),
			getClassFilePath("samples/jsr305/ExtendsMutableClass")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, times(2)).doReportBug(
			bugDefinition()
				.bugType("BROKEN_IMMUTABILITY")
				.inClass("BadImmutableClass")
				.build()
		);
		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("IMMUTABLE_CLASS_SHOULD_BE_FINAL")
				.inClass("BadImmutableClass")
				.build()
		);
	}
}

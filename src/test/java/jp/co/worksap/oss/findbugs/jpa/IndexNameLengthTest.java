package jp.co.worksap.oss.findbugs.jpa;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class IndexNameLengthTest extends BaseDetectorTest {
	private EasyBugReporter reporter;
	
	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testShortNameWithHibernate() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ShortIndexNameForHibernate")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("LONG_INDEX_NAME")
				.build()
		);
	}

	@Test
	public void testLongNameWithHibernate() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/LongIndexNameForHibernate")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("LONG_INDEX_NAME")
				.inClass("LongIndexNameForHibernate")
				.build()
		);
	}

	@Test
	public void testShortNameWithOpenJPA() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/ShortIndexNameForOpenJPA")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter, never()).doReportBug(
			bugDefinition()
				.bugType("LONG_INDEX_NAME")
				.build()
		);
	}

	@Test
	public void testLongNameWithOpenJPA() throws Exception {
		// Locate test code
		final String[] files = {
			getClassFilePath("samples/jpa/LongIndexNameForOpenJPA")
		};
		
		// Run the analysis
		analyze(files, reporter);

		verify(reporter).doReportBug(
			bugDefinition()
				.bugType("LONG_INDEX_NAME")
				.inClass("LongIndexNameForOpenJPA")
				.build()
		);
	}
}

package jp.co.worksap.oss.findbugs.jpa;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.umd.cs.findbugs.BugReporter;

@Ignore("test-driven-detectors4findbugs dependency is removed")
public class NullablePrimitiveDetectorTest {
	private BugReporter bugReporter;
	private NullablePrimitiveDetector detector;

	@Before
	public void before() {
		//        this.bugReporter = bugReporterForTesting();
		//        this.detector = new NullablePrimitiveDetector(bugReporter);
	}

	@Test
	public void testNullableObject() throws Exception {
		//        assertNoBugsReported(UseColumnDefinition.class, detector, bugReporter);
		//        assertNoBugsReported(ColumnWithoutElement.class, detector, bugReporter);
	}

	@Test
	public void testNullablePrimitive() throws Exception {
		//        assertBugReported(NullableBooleanColumn.class, detector,
		//                bugReporter, ofType("NULLABLE_PRIMITIVE"));
		//        assertBugReported(NullableByteColumn.class, detector,
		//                bugReporter, ofType("NULLABLE_PRIMITIVE"));
		//        assertBugReported(NullableShortColumn.class, detector,
		//                bugReporter, ofType("NULLABLE_PRIMITIVE"));
		//        assertBugReported(NullableIntColumn.class, detector,
		//                bugReporter, ofType("NULLABLE_PRIMITIVE"));
		//        assertBugReported(NullableLongColumn.class, detector,
		//                bugReporter, ofType("NULLABLE_PRIMITIVE"));
		//        assertBugReported(NullableFloatColumn.class, detector,
		//                bugReporter, ofType("NULLABLE_PRIMITIVE"));
		//        assertBugReported(NullableDoubleColumn.class, detector,
		//                bugReporter, ofType("NULLABLE_PRIMITIVE"));
		//        assertBugReported(NullableBooleanGetter.class, detector,
		//                bugReporter, ofType("NULLABLE_PRIMITIVE"));
	}

	@Test
	public void testNonNullableObject() throws Exception {
		//		assertNoBugsReported(ColumnWithNullable.class, detector, bugReporter);
	}

	@Test
	public void testNonNullableInt() throws Exception {
		//		assertNoBugsReported(NonNullablePrimitiveColumn.class, detector, bugReporter);
	}

}

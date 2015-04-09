package com.monits.findbugs.jdk;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.h3xstream.findbugs.test.BaseDetectorTest;
import com.h3xstream.findbugs.test.EasyBugReporter;

public class InconsistentHashCodeEqualsDetectorTest extends BaseDetectorTest {

	private EasyBugReporter reporter;

	@Before
	public void setup() {
		reporter = spy(new EasyBugReporter());
	}

	@Test
	public void testNoEqualsHashCodeErrors() throws Exception {
		final String[] files = {
				// Good implementation should not raise any errors
				getClassFilePath("samples/findbugs/GoodEqualsHashCodeImplementation"),
				// Classes with one overridden method should not raise any errors
				getClassFilePath("samples/findbugs/OnlyEqualsImplementation"),
				// Classes with neither methods overridden should not raise any errors
				getClassFilePath("samples/findbugs/NoEqualsHashCode"),
		};

		analyze(files, reporter);

		verify(reporter, never()).doReportBug(bugDefinition()
				.bugType("HASHCHODE_HAS_MORE_FIELDS_THAN_EQUALS")
				.build()
		);

		verify(reporter, never()).doReportBug(bugDefinition()
				.bugType("EQUALS_HAS_MORE_FIELDS_THAN_HASHCODE")
				.build()
		);
	}

	@Test
	public void testSubclassOfBadClassHasNoErrors() throws Exception {
		final String[] files = {
				getClassFilePath("samples/findbugs/HashCodeContainsEquals"),
				getClassFilePath("samples/findbugs/SubclassOfBadEqualsHashCode"),
		};

		analyze(files, reporter);

		// subclass must not have hashcode error ...
		verify(reporter, never()).doReportBug(
				bugDefinition()
				.bugType("HASHCHODE_HAS_MORE_FIELDS_THAN_EQUALS")
				.inClass("samples.findbugs.SubclassOfBadEqualsHashCode")
				.build()
		);

		// ... nor equals error
		verify(reporter, never()).doReportBug(
				bugDefinition()
				.bugType("EQUALS_HAS_MORE_FIELDS_THAN_HASHCODE")
				.inClass("samples.findbugs.SubclassOfBadEqualsHashCode")
				.build()
		);

		// superclass will rise hashcode error
		verify(reporter).doReportBug(
				bugDefinition()
				.bugType("HASHCHODE_HAS_MORE_FIELDS_THAN_EQUALS")
				.inClass("samples.findbugs.HashCodeContainsEquals")
				.atField("version")
				.build()
		);
	}

	@Test
	public void testHashCodeHasMoreFieldsThanEquals() throws Exception {
		final String[] files = {
				getClassFilePath("samples/findbugs/HashCodeContainsEquals"),
		};

		analyze(files, reporter);

		verify(reporter).doReportBug(
				bugDefinition()
				.bugType("HASHCHODE_HAS_MORE_FIELDS_THAN_EQUALS")
				.inClass("samples.findbugs.HashCodeContainsEquals")
				.inMethod("hashCode")
				.atField("version")
				.build()
		);
	}

	@Test
	public void testEqualsHasMoreFieldsThanHashCode() throws Exception {
		final String[] files = {
				getClassFilePath("samples/findbugs/EqualsContainsHashCode"),
		};

		analyze(files, reporter);

		verify(reporter).doReportBug(
				bugDefinition()
				.bugType("EQUALS_HAS_MORE_FIELDS_THAN_HASHCODE")
				.inClass("samples.findbugs.EqualsContainsHashCode")
				.inMethod("equals")
				.atField("version")
				.build()
		);
	}

	@Test
	public void testEqualsHashCodeDistinctFields() throws Exception {
		final String[] files = {
				getClassFilePath("samples/findbugs/EqualsHashCodeDifferentFields"),
		};

		analyze(files, reporter);

		verify(reporter).doReportBug(
				bugDefinition()
				.bugType("HASHCHODE_HAS_MORE_FIELDS_THAN_EQUALS")
				.inClass("samples.findbugs.EqualsHashCodeDifferentFields")
				.inMethod("hashCode")
				.atField("version")
				.build()
		);

		verify(reporter).doReportBug(
				bugDefinition()
				.bugType("EQUALS_HAS_MORE_FIELDS_THAN_HASHCODE")
				.inClass("samples.findbugs.EqualsHashCodeDifferentFields")
				.inMethod("equals")
				.atField("id")
				.build()
		);
	}

}

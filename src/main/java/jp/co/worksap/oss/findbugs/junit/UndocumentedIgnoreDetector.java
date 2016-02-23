package jp.co.worksap.oss.findbugs.junit;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.ElementValue;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.internalAnnotations.DottedClassName;

/***
 * UndocumentedIgnoreDetector detects if test cases are being ignored
 * without an explanation.
 */
public class UndocumentedIgnoreDetector extends BytecodeScanningDetector {

	private final BugReporter bugReporter;

	/***
	 * Creates an UndocumentedIgnoreDetector.
	 * @param bugReporter the bug reporter to use.
	 */
	public UndocumentedIgnoreDetector(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}

	@Override
	public void visitAnnotation(@DottedClassName final String annotationClass,
			final Map<String, ElementValue> map, final boolean runtimeVisible) {
		if (!"org.junit.Ignore".equals(annotationClass)) {
			return;
		}

		final ElementValue reason = map.get("value");
		if (reason == null || reason.stringifyValue().trim().isEmpty()) {
			final BugInstance bugInstance = new BugInstance("UNDOCUMENTED_IGNORE",
					HIGH_PRIORITY).addClass(this);
			if (visitingMethod()) {
				bugInstance.addMethod(this).addSourceLine(this);
			}
			bugReporter.reportBug(bugInstance);
		}
	}
}

package jp.co.worksap.oss.findbugs;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.Code;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * ForbiddenSystemClass detects uses of System.out and System.err,
 * logger classes should be used instead.
 */
public class ForbiddenSystemClass extends OpcodeStackDetector {
	private BugReporter bugReporter;

	/**
	 * Creates a new ForbiddenSystemClass detector.
	 * @param bugReporter the bug reporter to use.
	 */
	public ForbiddenSystemClass(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}

	@Override
	public void visit(final Code obj) {
		super.visit(obj);
	}

	@Override
	public void sawOpcode(final int seen) {
		if (seen == GETSTATIC) {
			if ("java/lang/System".equals(getClassConstantOperand())
					&& "out".equals(getNameConstantOperand())
					|| "err".equals(getNameConstantOperand())) {
				final BugInstance bug = new BugInstance(this, "FORBIDDEN_SYSTEM", NORMAL_PRIORITY)
						.addClassAndMethod(this).addSourceLine(this, getPC());
				bugReporter.reportBug(bug);
			}
		}
	}

}

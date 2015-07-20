package com.monits.findbugs.jdk;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class NonStaticPatternCompileDetector extends OpcodeStackDetector {

	public static final String NON_STATIC_PATTERN_COMPILE_CALL = "NON_STATIC_PATTERN_COMPILE_CALL";
	private static final String PATTERN_CLASSNAME = "java/util/regex/Pattern";
	private static final String COMPILE = "compile";

	private final BugReporter bugReporter;

	/**
	 * Report any non-static Pattern.compile
	 * @param bugReporter The BugReporter
	 */
	public NonStaticPatternCompileDetector(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}

	@Override
	public void sawOpcode(final int seen) {
		if (seen == INVOKESTATIC
				&& PATTERN_CLASSNAME.equals(getClassConstantOperand())
				&& COMPILE.equals(getFieldDescriptorOperand().getName())
				&& getNextOpcode() != PUTSTATIC
				// There is not a load instruction in the previous opcode
				// when the regex is final or static or hardwired.
				&& !isRegisterLoad(getPrevOpcode(1))) {
			this.bugReporter.reportBug(new BugInstance(this, NON_STATIC_PATTERN_COMPILE_CALL, NORMAL_PRIORITY)
				.addClassAndMethod(this)
				.addSourceLine(this));
		}
	}

	private boolean isRegisterLoad(final int prevOpcode) {
		return prevOpcode == ALOAD || prevOpcode == ALOAD_0 || prevOpcode == ALOAD_1
				|| prevOpcode == ALOAD_2 || prevOpcode == ALOAD_3;
	}
}
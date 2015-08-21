package com.monits.findbugs.jdk;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.OpcodeStack.Item;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

public class UselessStringValueOfCallDetector extends OpcodeStackDetector {

	public static final String USELESS_STRING_VALUEOF_CALL = "USELESS_STRING_VALUEOF_CALL";
	private static final String VALUE_OF = "valueOf";
	private static final Map<String, String> CLASSNAME_SIGNATURE_MAP;

	static {
		final Map<String, String> map = new HashMap<String, String>();
		map.put("java/lang/String", "Ljava/lang/String;");
		CLASSNAME_SIGNATURE_MAP = Collections.unmodifiableMap(map);
	}

	private final BugReporter bugReporter;

	/**
	 * Report any useless valueOf uses.
	 * @param bugReporter The BugReporter
	 */
	public UselessStringValueOfCallDetector(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}

	@Override
	public void sawOpcode(final int seen) {
		if (seen == INVOKESTATIC
				&& VALUE_OF.equals(getFieldDescriptorOperand().getName())
				&& isSameTypeArgumentAndClassOperand(getClassConstantOperand(), stack.getStackItem(0).getSignature())
				&& !isAConcatenationOfStrings()) {
			this.bugReporter.reportBug(new BugInstance(this, USELESS_STRING_VALUEOF_CALL, NORMAL_PRIORITY)
				.addClassAndMethod(this)
				.addSourceLine(this));
		}
	}

	private boolean isAConcatenationOfStrings() {
		final Item stackItem = stack.getStackItem(0);
		// check if the param is an undefined String
		return "Ljava/lang/String;".equals(stackItem.getSignature()) && stackItem.getConstant() == null
				// if the current string is not defined and is in a concatenation of strings,
				// then the compiler use an StringBuilder to put together those objects
				&& "Ljava/lang/StringBuilder;".equals(stack.getStackItem(1).getSignature());
	}

	private boolean isSameTypeArgumentAndClassOperand(final String classConstantOperand, final String argument) {
		return CLASSNAME_SIGNATURE_MAP.containsKey(classConstantOperand)
				&& argument.equals(CLASSNAME_SIGNATURE_MAP.get(classConstantOperand));
	}
}
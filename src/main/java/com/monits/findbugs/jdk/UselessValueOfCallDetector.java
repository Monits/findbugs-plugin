package com.monits.findbugs.jdk;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;

public class UselessValueOfCallDetector extends OpcodeStackDetector {

	public static final String USELESS_VALUEOF_CALL = "USELESS_VALUEOF_CALL";
	private static final String VALUE_OF = "valueOf";
	private static final Map<String, List<String>> CLASSNAME_SIGNATURE_MAP;

	static {
		final Map<String, List<String>> map = new HashMap<String, List<String>>();
		final String integerSignature = "I";
		map.put("java/lang/Integer", Arrays.asList(integerSignature));
		map.put("java/lang/String", Arrays.asList("Ljava/lang/String;"));
		map.put("java/lang/Long", Arrays.asList("J"));
		map.put("java/lang/Double", Arrays.asList("D"));
		map.put("java/lang/Float", Arrays.asList("F"));
		map.put("java/lang/Byte", Arrays.asList("B", integerSignature));
		map.put("java/lang/Short", Arrays.asList("S", integerSignature));
		map.put("java/lang/Character", Arrays.asList("C", integerSignature));
		CLASSNAME_SIGNATURE_MAP = Collections.unmodifiableMap(map);
	}

	private final BugReporter bugReporter;

	/**
	 * Report any useless valueOf uses.
	 * @param bugReporter The BugReporter
	 */
	public UselessValueOfCallDetector(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}

	@Override
	public void sawOpcode(final int seen) {
		if (seen == INVOKESTATIC) {
			final FieldDescriptor operand = getFieldDescriptorOperand();
			if (VALUE_OF.equals(operand.getName())
					&& isSameTypeArgumentAndClassOperand(getClassConstantOperand(),
							stack.getStackItem(0).getSignature())) {
				this.bugReporter.reportBug(new BugInstance(this, USELESS_VALUEOF_CALL, NORMAL_PRIORITY)
					.addClassAndMethod(this)
					.addSourceLine(this));
			}
		}
	}

	private boolean isSameTypeArgumentAndClassOperand(final String classConstantOperand, final Object argument) {
		return CLASSNAME_SIGNATURE_MAP.containsKey(classConstantOperand)
				&& CLASSNAME_SIGNATURE_MAP.get(classConstantOperand).contains(argument);
	}
}
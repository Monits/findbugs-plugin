package com.monits.findbugs.jdk;

import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.BasicBlock;
import edu.umd.cs.findbugs.ba.BasicBlock.InstructionIterator;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Edge;
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.ba.XMethod;

public class InconsistentHashCodeEqualsDetector extends BytecodeScanningDetector {

	private static final String HASH_CODE_METHOD_NAME = "hashCode";
	private static final String EQUALS_METHOD_NAME = "equals";
	private static final String EQUALS_SIGNATURE = "(Ljava/lang/Object;)Z";

	private XMethodAndFields equalsFields;
	private XMethodAndFields hashCodeFields;
	private Map<String, XField> fieldNameToXField;

	private final BugReporter reporter;

	public InconsistentHashCodeEqualsDetector(@Nonnull final BugReporter reporter) {
		this.reporter = reporter;
	}

	@Override
	public void visitClassContext(final ClassContext classContext) {
		if (qualifiesForDetection(classContext)) {
			fieldNameToXField = populateNamesAndXFields(classContext);
			super.visitClassContext(classContext);

			if (!hashCodeFields.getFieldNames().equals(equalsFields.getFieldNames())) {
				reportBugs("HASHCHODE_HAS_MORE_FIELDS_THAN_EQUALS", HIGH_PRIORITY, hashCodeFields.getXMethod(),
						getFieldsDifference(hashCodeFields.getFieldNames(), equalsFields.getFieldNames()));

				reportBugs("EQUALS_HAS_MORE_FIELDS_THAN_HASHCODE", HIGH_PRIORITY, equalsFields.getXMethod(),
						getFieldsDifference(equalsFields.getFieldNames(), hashCodeFields.getFieldNames()));
			}

			equalsFields = null;
			hashCodeFields = null;
			fieldNameToXField = null;
		}
	}

	@Nonnull
	private Map<String, XField> populateNamesAndXFields(@Nonnull final ClassContext classContext) {
		final Map<String, XField> nameToXField = new HashMap<String, XField>();
		for (final XField xField : classContext.getXClass().getXFields()) {
			if (!xField.isStatic() && !xField.isSynthetic()) {
				nameToXField.put(xField.getName(), xField);
			}
		}
		return nameToXField;
	}

	@Nonnull
	private Set<XField> getFieldsDifference(@Nonnull final Set<XField> comparable,
			@Nonnull final Set<XField> comparator) {

		final Set<XField> copyComparable = new HashSet<XField>(comparable);
		for (final XField xField : comparator) {
			copyComparable.remove(xField);
		}

		return copyComparable;
	}

	private boolean qualifiesForDetection(@Nonnull final ClassContext ctx) {
		int methodCounter = 0;
		for (final Method m : ctx.getMethodsInCallOrder()) {
			if (isEqualsMethod(m) || isHashCodeMethod(m)) {
				methodCounter++;
			}
		}

		// must be 2, hashCode and equals methods
		return methodCounter == 2;
	}

	private void reportBugs(@Nonnull final String bugType, @Nonnull final int priority, @Nonnull final XMethod method,
			@Nonnull final Set<XField> fields) {

		for (final XField xField : fields) {
			final BugInstance bug = new BugInstance(this, bugType, priority)
				.addClass(getClassContext().getClassDescriptor())
				.addMethod(method)
				.addField(xField);

			reporter.reportBug(bug);
		}
	}

	@Override
	public void visitMethod(final Method method) {
		if (isEqualsMethod(method)) {
			equalsFields = new XMethodAndFields(getXMethod(), getMethodXFields(method));
		} else if (isHashCodeMethod(method)) {
			hashCodeFields = new XMethodAndFields(getXMethod(), getMethodXFields(method));
		}
	}

	private boolean isEqualsMethod(@Nonnull final Method method) {
		return EQUALS_METHOD_NAME.equals(method.getName()) && EQUALS_SIGNATURE.equals(method.getSignature());
	}

	private boolean isHashCodeMethod(@Nonnull final Method method) {
		return HASH_CODE_METHOD_NAME.equals(method.getName()) && method.getArgumentTypes().length == 0;
	}

	@Nonnull
	private Set<XField> getMethodXFields(@Nonnull final Method method) {
		final Set<XField> xFields = new HashSet<XField>();

		final CFG cfg;
		final ConstantPoolGen cpg;
		final BasicBlock bb;

		try {
			cfg = getClassContext().getCFG(method);
			cpg = cfg.getMethodGen().getConstantPool();
			bb = cfg.getEntry();
		} catch (final CFGBuilderException cbe) {
			return xFields;
		}

		final BitSet visitedBlock = new BitSet();
		final Deque<BasicBlock> toBeProcessed = new LinkedList<BasicBlock>();
		toBeProcessed.add(bb);

		while (!toBeProcessed.isEmpty()) {
			final BasicBlock currentBlock = toBeProcessed.removeFirst();
			final InstructionIterator ii = currentBlock.instructionIterator();

			while (ii.hasNext()) {
				final InstructionHandle ih = ii.next();
				final Instruction ins = ih.getInstruction();

				if (ins instanceof FieldInstruction) {
					final FieldInstruction fi = (FieldInstruction) ins;
					final String fieldName = fi.getFieldName(cpg);

					if (ins instanceof GETFIELD) {
						// TODO : Make sure we are actually using it to compute hashCode / equals
						final XField xField = fieldNameToXField.get(fieldName);
						if (null != xField) {
							// add field metadata
							xFields.add(xField);
						}
					}
				}
				// TODO : Check for INVOKESPECIAL / INVOKEVIRTUAL calling toString from other objects
			}

			// Get adjacent blocks
			final Iterator<Edge> oei = cfg.outgoingEdgeIterator(currentBlock);
			while (oei.hasNext()) {
				final Edge e = oei.next();
				final BasicBlock cb = e.getTarget();
				final int label = cb.getLabel();

				// Avoid repeated blocks
				if (!visitedBlock.get(label)) {
					toBeProcessed.addLast(cb);
					visitedBlock.set(label);
				}
			}
		}

		return xFields;
	}

	private static final class XMethodAndFields {
		private final XMethod method;
		private final Set<XField> fieldNames;

		protected XMethodAndFields(final XMethod method, final Set<XField> fieldNames) {
			this.method = method;
			this.fieldNames = fieldNames;
		}

		public XMethod getXMethod() {
			return method;
		}

		public Set<XField> getFieldNames() {
			return fieldNames;
		}
	}

}

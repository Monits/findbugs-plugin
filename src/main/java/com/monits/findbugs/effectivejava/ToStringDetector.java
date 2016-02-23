package com.monits.findbugs.effectivejava;

import java.util.BitSet;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import edu.umd.cs.findbugs.ba.AnalysisContext;
import edu.umd.cs.findbugs.ba.BasicBlock;
import edu.umd.cs.findbugs.ba.BasicBlock.InstructionIterator;
import edu.umd.cs.findbugs.ba.CFG;
import edu.umd.cs.findbugs.ba.CFGBuilderException;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Edge;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.ba.XField;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.CheckedAnalysisException;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.MissingClassException;
import edu.umd.cs.findbugs.classfile.analysis.AnnotatedObject;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;

/**
 * ToStringDetector tries to detect issues with ToString() method.
 *
 * This detector will warn you if:
 *
 * - Your class has internal state but doesn't override ToString() method.
 *
 * - Your class has an internal variable that is not used in ToString() method.
 *
 * Enums and static fields are ignored.
 *
 * Interfaces and external libraries without ToString() are ignored too.
 */
public class ToStringDetector extends BytecodeScanningDetector {

	public static final String MISSING_FIELD_IN_TO_STRING = "MISSING_FIELD_IN_TO_STRING";
	public static final String MISSING_TO_STRING_OVERRIDE = "MISSING_TO_STRING_OVERRIDE";

	private static final String TO_STRING = "toString";
	private static final String JAVA_LANG_ENUM = "java.lang.Enum";
	private static final ClassDescriptor SUPPRESS_FB_WARNING_CD = DescriptorFactory
			.createClassDescriptor(SuppressFBWarnings.class);

	private final BugReporter bugReporter;

	@SuppressFBWarnings(value = "PMB_POSSIBLE_MEMORY_BLOAT", justification = "We need this as database.")
	private static final Map<String, Boolean> IS_INTERESTING_CLASS_CACHE = new HashMap<>();
	private Map<String, XField> interestFields;

	private boolean hasToStringOverride;

	/**
	 * Creates a new ToStringDetector.
	 *
	 * @param bugReporter
	 *            the Findbugs bug reporter.
	 *
	 */
	public ToStringDetector(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = bugReporter;

		// Known interesting classes
		IS_INTERESTING_CLASS_CACHE.put("java/lang/String", Boolean.TRUE);
	}

	@Override
	public void visitClassContext(@Nonnull final ClassContext classContext) {
		try {
			final XClass xClass = classContext.getXClass();

			if (xClass == null) {
				return;
			}

			// Never report on enums, they are good as they are
			final ClassDescriptor superCD = xClass.getSuperclassDescriptor();
			if (superCD != null && JAVA_LANG_ENUM.equals(superCD.getDottedClassName())) {
				return;
			}

			interestFields = getInterestingFields(xClass);

			if (!interestFields.isEmpty()) {
				// Continue with analysis....
				super.visitClassContext(classContext);

				// ... and check if any fields were not included in toString
				if (!interestFields.isEmpty()) {
					if (hasToStringOverride) {
						for (final Entry<String, XField> entry : interestFields.entrySet()) {
							final BugInstance bug = new BugInstance(this, MISSING_FIELD_IN_TO_STRING, NORMAL_PRIORITY)
									.addClass(this).addField(entry.getValue());
							bugReporter.reportBug(bug);
						}
					} else {
						final BugInstance bug = new BugInstance(this, MISSING_TO_STRING_OVERRIDE, NORMAL_PRIORITY)
								.addClass(this);
						bugReporter.reportBug(bug);
					}
				}
			}
		} catch (final CheckedAnalysisException e) {
			if (e instanceof MissingClassException) {
				AnalysisContext.reportMissingClass((MissingClassException) e);
			}
		} finally {
			interestFields = null;
			hasToStringOverride = false;
		}
	}

	@Nonnull
	private Map<String, XField> getInterestingFields(@Nonnull final XClass xClass) throws CheckedAnalysisException {
		final List<? extends XField> fields = xClass.getXFields();
		final Map<String, XField> toStringFields = new HashMap<String, XField>();

		for (final XField f : fields) {
			if (!f.isStatic() && !f.isSynthetic()) {
				if (isIgnored(f, MISSING_FIELD_IN_TO_STRING)) {
					continue;
				}

				if (f.isReferenceType()) {
					final String signature = f.getSignature();
					final ClassDescriptor fieldClassDescriptor = DescriptorFactory
							.createClassDescriptorFromFieldSignature(signature);
					if (fieldClassDescriptor == null) {
						// This is an array of primitives, interesting by
						// default
						toStringFields.put(f.getName(), f);
					} else {
						// Field classes are analyzed recursively
						if (isClassFieldAnInterestingField(fieldClassDescriptor)) {
							toStringFields.put(f.getName(), f);
						}
					}
				} else {
					// primitives are interesting by default!
					toStringFields.put(f.getName(), f);
				}
			}
		}

		return toStringFields;
	}

	private static boolean isIgnored(@Nonnull final AnnotatedObject ao, @Nonnull final String error) {
		final AnnotationValue suppressAnnotation = ao.getAnnotation(ToStringDetector.SUPPRESS_FB_WARNING_CD);

		if (suppressAnnotation != null) {
			final Object[] values = (Object[]) suppressAnnotation.getValue("value");
			for (final Object v : values) {
				if (error.equals(v)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isClassFieldAnInterestingField(@Nonnull final ClassDescriptor fieldClassDescriptor)
			throws CheckedAnalysisException {
		final XClass fieldXClass = fieldClassDescriptor.getXClass();

		if (AnalysisContext.currentAnalysisContext().isApplicationClass(fieldClassDescriptor)) {
			// It's an Application fields, check if it needs a toString itself.
			return !isIgnored(fieldXClass, MISSING_TO_STRING_OVERRIDE) && isStatefullClass(fieldXClass);
		} else {
			// For non-application fields, just check if they provide a
			// toString() override.
			final XMethod toString = fieldXClass.findMethod(TO_STRING, "()Ljava/lang/String;", false);
			return toString != null && !"java.lang.Object".equals(toString.getClassName());
		}
	}

	private boolean isStatefullClass(@Nonnull final XClass xClass) throws CheckedAnalysisException {
		final String className = xClass.getClassDescriptor().getClassName();
		if (IS_INTERESTING_CLASS_CACHE.containsKey(className)) {
			return IS_INTERESTING_CLASS_CACHE.get(className);
		}

		// Is it an enum?
		final ClassDescriptor superCD = xClass.getSuperclassDescriptor();
		if (superCD != null && JAVA_LANG_ENUM.equals(superCD.getDottedClassName())) {
			IS_INTERESTING_CLASS_CACHE.put(className, Boolean.TRUE);
			return true;
		}

		// Default to false in case of cross references between classes...
		IS_INTERESTING_CLASS_CACHE.put(className, Boolean.FALSE);

		final Map<String, XField> interestingFields = getInterestingFields(xClass);
		final Boolean ret = interestingFields.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
		IS_INTERESTING_CLASS_CACHE.put(className, ret);

		return ret;
	}

	@Override
	public String toString() {
		return "ToStringDetector [bugReporter=" + bugReporter + ", interestFields=" + interestFields
				+ ", hasToStringOverride=" + hasToStringOverride + "]";
	}

	@Override
	public void visitMethod(@Nonnull final Method method) {
		// Is this toString?
		if (TO_STRING.equals(method.getName()) && method.getArgumentTypes().length == 0) {
			hasToStringOverride = true;

			try {
				final CFG cfg = getClassContext().getCFG(method);
				final ConstantPoolGen cpg = cfg.getMethodGen().getConstantPool();
				final BasicBlock bb = cfg.getEntry();
				checkBlock(bb, cpg, cfg);
			} catch (final CFGBuilderException cbe) {
				interestFields.clear();
			}
		}
	}

	private void checkBlock(@Nonnull final BasicBlock bb, @Nonnull final ConstantPoolGen cpg, @Nonnull final CFG cfg) {
		final BitSet visitedBlock = new BitSet();
		final Deque<BasicBlock> toBeProcessed = new LinkedList<BasicBlock>();
		toBeProcessed.add(bb);

		while (!toBeProcessed.isEmpty()) {
			final BasicBlock currentBlock = toBeProcessed.removeFirst();
			final InstructionIterator ii = currentBlock.instructionIterator();

			while (!interestFields.isEmpty() && ii.hasNext()) {
				final InstructionHandle ih = ii.next();
				final Instruction ins = ih.getInstruction();

				if (ins instanceof FieldInstruction) {
					final FieldInstruction fi = (FieldInstruction) ins;
					final String fieldName = fi.getFieldName(cpg);

					if (ins instanceof GETFIELD) {
						// TODO : Make sure we are actually using it to place it in the string representation
						interestFields.remove(fieldName);
					}
				}
				// TODO : Check for INVOKESPECIAL / INVOKEVIRTUAL calling toString from other objects
			}

			if (interestFields.isEmpty()) {
				return;
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
	}
}

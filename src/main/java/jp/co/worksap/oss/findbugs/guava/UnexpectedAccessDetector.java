package jp.co.worksap.oss.findbugs.guava;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.ba.Hierarchy;
import edu.umd.cs.findbugs.ba.XClass;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;

/**
 * <p>
 * A detector to ensure that implementation (class in src/main/java) doesn't
 * call package-private method in other class which is annotated by
 * {@code @VisibleForTesting}.
 * </p>
 *
 * @author Kengo TODA (toda_k@worksap.co.jp)
 * @see com.google.common.annotations.VisibleForTesting
 */
public class UnexpectedAccessDetector extends BytecodeScanningDetector {
	@Nonnull
	private final BugReporter bugReporter;

	/**
	 * @param bugReporter The BugReporter to be used.
	 */
	public UnexpectedAccessDetector(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = checkNotNull(bugReporter);
	}

	@Override
	public void visitClassContext(@Nonnull final ClassContext classContext) {
		final XClass currentClass = classContext.getXClass();

		try {
			if (Hierarchy.isSubtype(currentClass.getClassDescriptor()
					.getDottedClassName(), "junit.framework.TestCase")) {
				// no need to check, because method is called by JUnit's 3
				// TestCase method
				return;
			}

			// Check if this is a JUnit 4 test class
			final List<? extends XMethod> xMethods = currentClass.getXMethods();
			for (final XMethod xm : xMethods) {
				for (final ClassDescriptor acd : xm.getAnnotationDescriptors()) {
					if (acd.getDottedClassName().startsWith("org.junit.")) {
						return;
					}
				}
			}

			// We want to check this class' opcodes
			super.visitClassContext(classContext);
		} catch (final ClassNotFoundException e) {
			bugReporter.reportMissingClass(e);
		}
	}

	@Override
	public void sawOpcode(final int opcode) {
		if (!isInvoking(opcode)) {
			return;
		}

		final ClassDescriptor currentClass = getClassDescriptor();
		final ClassDescriptor invokedClass = getClassDescriptorOperand();

		if (currentClass.equals(invokedClass)) {
			// no need to check, because method is called by owner
			return;
		}
		
		if (!currentClass.getPackageName().equals(
				invokedClass.getPackageName())) {
			// no need to check, because method is called by class in other
			// package
			return;
		}

		final XMethod invokedMethod = getXMethodOperand();
		if (invokedMethod != null) {
			verifyVisibility(invokedMethod);
		}
	}

	/**
	 * <p>
	 * Report if specified method is package-private and annotated by
	 * {@code @VisibleForTesting}.
	 * </p>
	 */
	private void verifyVisibility(@Nonnull final XMethod invokedMethod) {
		if (checkVisibility(invokedMethod) && checkAnnotated(invokedMethod)) {
			final BugInstance bug = new BugInstance(this,
					"GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING",
					HIGH_PRIORITY);

			bug.addCalledMethod(this).addClassAndMethod(this)
					.addSourceLine(this);
			bugReporter.reportBug(bug);
		}
	}

	/**
	 * @return true if visibility of specified method is package-private.
	 */
	private boolean checkVisibility(@Nonnull final XMethod bcelMethod) {
		return !(bcelMethod.isPrivate() || bcelMethod.isProtected() || bcelMethod
				.isPublic());
	}

	/**
	 * @return true if specified method is annotated by
	 *         {@code VisibleForTesting}.
	 */
	private boolean checkAnnotated(@Nonnull final XMethod bcelMethod) {
		for (final AnnotationValue annotation : bcelMethod.getAnnotations()) {
			final String type = annotation.getAnnotationClass().getSignature();
			if ("Lcom/google/common/annotations/VisibleForTesting;"
					.equals(type)) {
				return true;
			}
		}
		return false;
	}

	private boolean isInvoking(final int opcode) {
		return opcode == INVOKESPECIAL || opcode == INVOKEINTERFACE
				|| opcode == INVOKESTATIC || opcode == INVOKEVIRTUAL;
	}

}

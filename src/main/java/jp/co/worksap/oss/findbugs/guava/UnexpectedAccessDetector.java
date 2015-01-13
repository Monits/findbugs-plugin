package jp.co.worksap.oss.findbugs.guava;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;

/**
 * <p>A detector to ensure that implementation (class in src/main/java) doesn't call
 * package-private method in other class which is annotated by {@code @VisibleForTesting}.</p>
 *
 * @author Kengo TODA (toda_k@worksap.co.jp)
 * @see com.google.common.annotations.VisibleForTesting
 */
public class UnexpectedAccessDetector extends BytecodeScanningDetector {
	@Nonnull
	private final BugReporter bugReporter;
	
	private static final ClassDescriptor junitTestClassDescriptor = DescriptorFactory.createClassDescriptor("org/junit/Test");

	/**
	 * @param bugReporter
	 */
	public UnexpectedAccessDetector(BugReporter bugReporter) {
		this.bugReporter = checkNotNull(bugReporter);
	}

	@Override
	public void sawOpcode(int opcode) {
		if (!isInvoking(opcode)) {
			return;
		}

		ClassDescriptor currentClass = getClassDescriptor();
		ClassDescriptor invokedClass = getClassDescriptorOperand();
		
		if (currentClass.equals(invokedClass)) {
			// no need to check, because method is called by owner
		} else if (!currentClass.getPackageName().equals(invokedClass.getPackageName())) {
			// no need to check, because method is called by class in other package
		} else if (getXMethod().getAnnotation(junitTestClassDescriptor) != null) {
			// no need to check, because method is called by @Test method
		} else {
			XMethod invokedMethod = getXMethodOperand();

			try {
				verifyVisibility(invokedClass, invokedMethod, true);
			} catch (ClassNotFoundException e) {
				String message = String.format("Detector could not find %s, you should add this class into CLASSPATH", invokedClass.getDottedClassName());
				bugReporter.logError(message, e);
			}
		}
	}

	/**
	 * <p>Report if specified method is package-private and annotated by {@code @VisibleForTesting}.</p>
	 */
	private void verifyVisibility(ClassDescriptor invokedClass, XMethod invokedMethod, boolean reportCaller) throws ClassNotFoundException {
		if (checkVisibility(invokedMethod) && checkAnnotated(invokedMethod)) {
			BugInstance bug = new BugInstance(this, "GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING", HIGH_PRIORITY);
			if (reportCaller) {
				bug.addCalledMethod(this).addClassAndMethod(this).addSourceLine(this);
			}
			bugReporter.reportBug(bug);
		}
	}

	/**
	 * @return true if visibility of specified method is package-private.
	 */
	private boolean checkVisibility(XMethod bcelMethod) {
		return !(bcelMethod.isPrivate() || bcelMethod.isProtected() || bcelMethod.isPublic());
	}

	/**
	 * @return true if specified method is annotated by {@code VisibleForTesting}.
	 */
	private boolean checkAnnotated(XMethod bcelMethod) {
		for (AnnotationValue annotation : bcelMethod.getAnnotations()) {
			String type = annotation.getAnnotationClass().getSignature();
			if ("Lcom/google/common/annotations/VisibleForTesting;".equals(type)) {
				return true;
			}
		}
		return false;
	}

	private boolean isInvoking(int opcode) {
		return opcode == INVOKESPECIAL || opcode == INVOKEINTERFACE || opcode == INVOKESTATIC || opcode == INVOKEVIRTUAL;
	}

}

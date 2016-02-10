package jp.co.worksap.oss.findbugs.jpa;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.bcel.classfile.FieldOrMethod;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import edu.umd.cs.findbugs.bcel.AnnotationDetector;

/**
 * <p>
 * Simple ClassVisitor implementation to find visited field in the specified
 * method.
 * </p>
 * <p>
 * To create instance, you need to provide name and descriptor to specify the
 * target method.
 * </p>
 *
 * @author Kengo TODA
 */
final class VisitedFieldFinder extends ClassVisitor {
	private final class MethodVisitorExtension extends MethodVisitor {
		private MethodVisitorExtension(final int api) {
			super(api);
		}

		@Override
		public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
			visitedFieldName = name;
			// super.visitFieldInsn(opcode, owner, name, desc);
		}
	}

	private static final int API_VERSION = Opcodes.ASM5;
	private final String targetMethodName;
	private final String targetMethodDescriptor;

	private String visitedFieldName;

	VisitedFieldFinder(@Nonnull final String targetMethodName, @Nonnull final String targetMethodDescriptor) {
		super(API_VERSION);
		this.targetMethodName = checkNotNull(targetMethodName);
		this.targetMethodDescriptor = checkNotNull(targetMethodDescriptor);
	}

	@Override
	public String toString() {
		return "VisitedFieldFinder [targetMethodName=" + targetMethodName + ", targetMethodDescriptor="
				+ targetMethodDescriptor + ", visitedFieldName=" + visitedFieldName + "]";
	}

	@CheckReturnValue
	@Nullable
	private String getVisitedFieldName() {
		return visitedFieldName;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String descriptor,
			final String signature, final String[] exceptions) {
		if (name.equals(targetMethodName) && descriptor.equals(targetMethodDescriptor)) {
			return new MethodVisitorExtension(API_VERSION);
		} else {
			// We do not have to analyze this method.
			// Returning null let ASM skip parsing this method.
			return null;
		}
	}

	// @Override
	// public FieldVisitor visitField(int access, String name, String desc,
	// String signature, Object value) {
	// return null;
	//
	// }

	// @Override
	// public void visitFieldInsn(int code, String owner, String name, String
	// description) {
	// visitedFieldName = name;
	// }

	@Nullable
	@CheckReturnValue
	static String findFieldWhichisVisitedInVisitingMethod(final AnnotationDetector detector) {
		final byte[] classByteCode = detector.getClassContext().getJavaClass().getBytes();
		final ClassReader reader = new ClassReader(classByteCode);

		final FieldOrMethod targetMethod = detector.getMethod();
		// note: bcel's #getSignature() method returns String like "(J)V", this
		// is named as "descriptor" in the context of ASM.
		// This is the reason why we call `targetMethod.getSignature()` to get
		// value for `targetMethodDescriptor` argument.
		final VisitedFieldFinder visitedFieldFinder = new VisitedFieldFinder(targetMethod.getName(),
				targetMethod.getSignature());
		reader.accept(visitedFieldFinder, 0);
		return visitedFieldFinder.getVisitedFieldName();
	}
}

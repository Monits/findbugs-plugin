package com.monits.findbugs.effectivejava;

import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.Method;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.ba.Hierarchy2;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;

public class EqualsOverrideDetector extends BytecodeScanningDetector {

	private final static ClassDescriptor OBJECT_CLASS_DESCRIPTOR
		= DescriptorFactory.createClassDescriptor(Object.class);

	private final BugReporter bugReporter;

	public EqualsOverrideDetector(@Nonnull final BugReporter bugReporter) {
		this.bugReporter = bugReporter;
	}
	
	@Override
	public void visitMethod(@Nonnull final Method method) {
		// Is this equals?
		if ("equals".equals(method.getName()) && "(Ljava/lang/Object;)Z".equals(method.getSignature())) {
			final Set<XMethod> superMethods = Hierarchy2.findSuperMethods(getXMethod());
			
			for (final XMethod xm : superMethods) {
				final ClassDescriptor classDescriptor = xm.getClassDescriptor();
				if (!OBJECT_CLASS_DESCRIPTOR.equals(classDescriptor)) {
					final BugInstance bug = new BugInstance(this, "DONT_OVERRIDE_EQUALS", NORMAL_PRIORITY)
						.addClass(this)
						.addClass(classDescriptor);
					bugReporter.reportBug(bug);
					return;
				}
			}
		}
	}
}

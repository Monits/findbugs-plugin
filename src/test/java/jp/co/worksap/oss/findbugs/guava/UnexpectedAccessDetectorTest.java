package jp.co.worksap.oss.findbugs.guava;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import org.apache.bcel.Constants;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.objectweb.asm.Opcodes;

import com.google.common.annotations.VisibleForTesting;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.XMethod;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.analysis.AnnotationValue;
import edu.umd.cs.findbugs.classfile.analysis.MethodInfo;
import edu.umd.cs.findbugs.classfile.analysis.MethodInfo.Builder;


/**
 * @author tolina GmbH
 *
 */
public class UnexpectedAccessDetectorTest {


	@Mock
	private BugReporter bugReporter;

	/**
	 * 
	 */
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		//		bugReporter = bugReporterForTesting();
	}

	@Test
	@Ignore
	public void testNormalMethod() throws Exception {
		//assertNoBugsReported(ClassWhichCallsNormalMethod.class, detector, bugReporter);
	}

	@Test
	@Ignore
	public void testCallingAnnotatedMethod() throws Exception {
		//		assertBugReported(ClassWhichCallsVisibleMethodForTesting.class, detector, bugReporter, ofType("GUAVA_UNEXPECTED_ACCESS_TO_VISIBLE_FOR_TESTING"));
	}

	/**
	 * Tests reporting a Bug when:
	 * - called Method has 'default'-visibility
	 * - called Method is annotated with {@link VisibleForTesting}
	 * @throws Exception
	 */
	@Test
	public void testVerifyVisibility() throws Exception {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		ClassDescriptor invokedClassDescriptor;
		XMethod invokedMethodDescriptor;

		invokedClassDescriptor = DescriptorFactory.createClassDescriptor(Object.class);
		invokedMethodDescriptor = new MethodInfo.Builder("java/lang/Object", "equals", "(Ljava/lang/Object;)Z", 0).build();
		detector.verifyVisibility(invokedClassDescriptor, invokedMethodDescriptor, false);

		invokedClassDescriptor = DescriptorFactory.createClassDescriptor(TestClass.class);
		Builder builder = new MethodInfo.Builder("jp/co/worksap/oss/findbugs/guava/TestClass", "test", "()V", 0);
		builder.addAnnotation("Lcom/google/common/annotations/VisibleForTesting;", new AnnotationValue("com/google/common/annotations/VisibleForTesting"));
		detector.verifyVisibility(invokedClassDescriptor, builder.build(), false);
		
		verify(bugReporter).reportBug(any(BugInstance.class));
	}

	/**
	 * Checks, if detecting of 'default'-Visibility works
	 */
	@Test
	public void testCheckVisibility() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		Builder methodBuilder = new MethodInfo.Builder("java/lang/Object", "equals", "(Ljava/lang/Object;)Z", Opcodes.ACC_PRIVATE);
		assertFalse(detector.checkVisibility(methodBuilder.build()));

		methodBuilder.setAccessFlags(Opcodes.ACC_PROTECTED);
		assertFalse(detector.checkVisibility(methodBuilder.build()));

		methodBuilder.setAccessFlags(Opcodes.ACC_PUBLIC);
		assertFalse(detector.checkVisibility(methodBuilder.build()));

		methodBuilder.setAccessFlags(0);
		assertTrue(detector.checkVisibility(methodBuilder.build()));
	}

	/**
	 * Checks, if detecting of annotated Methods works 
	 */
	@Test
	public void testCheckAnnotated() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		Builder methodBuilder = new MethodInfo.Builder("java/lang/Object", "equals", "(Ljava/lang/Object;)Z", Opcodes.ACC_PUBLIC);

		assertFalse(detector.checkAnnotated(methodBuilder.build()));

		methodBuilder.addAnnotation("Lorg/junit/Test;", new AnnotationValue("org/junit/Test"));
		assertFalse(detector.checkAnnotated(methodBuilder.build()));

		methodBuilder.addAnnotation("Lcom/google/common/annotations/VisibleForTesting;", new AnnotationValue("com/google/common/annotations/VisibleForTesting"));
		assertTrue(detector.checkAnnotated(methodBuilder.build()));
	}

	/**
	 * Checks, if 'invoking' OP-Codes are detected
	 */
	@Test
	public void testIsInvoking() {
		UnexpectedAccessDetector detector = new UnexpectedAccessDetector(bugReporter);
		assertTrue(detector.isInvoking(Constants.INVOKESPECIAL));
		assertTrue(detector.isInvoking(Constants.INVOKEINTERFACE));
		assertTrue(detector.isInvoking(Constants.INVOKESTATIC));
		assertTrue(detector.isInvoking(Constants.INVOKEVIRTUAL));

	}

}

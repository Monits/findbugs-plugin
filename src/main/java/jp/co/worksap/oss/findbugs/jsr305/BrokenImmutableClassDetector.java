package jp.co.worksap.oss.findbugs.jsr305;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;

import com.google.common.base.Objects;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import edu.umd.cs.findbugs.internalAnnotations.DottedClassName;

/**
 * <p>Detector to check immutability of class.</p>
 * <p>Note that this detector does not ensure that &quot;constructor stores deep-copied instance&quot; and
 * &quot;getter returns deep-copied instance&quot;. Use EI_EXPOSE_REP and EI_EXPOSE_REP2 to check it.</p>
 *
 * @see http://findbugs.sourceforge.net/bugDescriptions.html#EI_EXPOSE_REP
 * @see http://findbugs.sourceforge.net/bugDescriptions.html#EI_EXPOSE_REP2
 * @author Kengo TODA
 */
public class BrokenImmutableClassDetector extends AnnotationDetector {

    private final BugReporter reporter;

	/**
	* Create a new BrokenImmutableClassDetector.
	* @param reporter the Bug Reporter to use.
	*/
	public BrokenImmutableClassDetector(@Nonnull final BugReporter reporter) {
		this.reporter = reporter;
	}

    @Override
    public void visitAnnotation(@DottedClassName String annotationClass,
            Map<String, ElementValue> map, boolean runtimeVisible) {
        if (!Objects.equal(annotationClass, "javax.annotation.concurrent.Immutable")) {
            return;
        }

        JavaClass targetClass = getThisClass();
        if (!targetClass.isFinal()) {
            reporter.reportBug(new BugInstance(this, "IMMUTABLE_CLASS_SHOULD_BE_FINAL", HIGH_PRIORITY).addClass(this));
        }

        try {
            checkImmutability(targetClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find super class of " + targetClass.getClassName() + ". Check classpath.", e);
        }
    }

	private void checkImmutability(@Nullable final JavaClass immutableClass) throws ClassNotFoundException {
		if (immutableClass == null) {
			return;
		}
		for (final Field field : immutableClass.getFields()) {
			if (!field.isStatic() && !field.isFinal()) {
				reporter.reportBug(new BugInstance(this, "BROKEN_IMMUTABILITY", HIGH_PRIORITY)
					.addClass(immutableClass)
					.addString(field.getName())
					.addString(immutableClass.getClassName())
					.addString(getThisClass().getClassName()));
			}
		}
		checkImmutability(immutableClass.getSuperClass());
	}
}

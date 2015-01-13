package jp.co.worksap.oss.findbugs.findbugs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.ElementValue;

import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.internalAnnotations.DottedClassName;

/**
 * <p>A detector to ensure that FindBugs&apos; SuppressWarnings annotation has justification.</p>
 * @see edu.umd.cs.findbugs.annotations.SuppressWarnings
 * @see edu.umd.cs.findbugs.annotations.SuppressFBWarnings
 * @author Kengo TODA (toda_k@worksap.co.jp)
 */
public class UndocumentedSuppressFBWarningsDetector extends BytecodeScanningDetector {
    private static final Set<String> TARGET_ANNOTATIONS = Collections.unmodifiableSet(Sets.newHashSet(
            "edu.umd.cs.findbugs.annotations.SuppressWarnings",
            "edu.umd.cs.findbugs.annotations.SuppressFBWarnings"
    ));

    @Nonnull
    private final BugReporter bugReporter;

    public UndocumentedSuppressFBWarningsDetector(BugReporter bugReporter) {
        this.bugReporter = checkNotNull(bugReporter);
    }

    @Override
    public void visitAnnotation(@DottedClassName String annotationClass,
            Map<String, ElementValue> map, boolean runtimeVisible) {
        if (!TARGET_ANNOTATIONS.contains(annotationClass)) {
            return;
        }

        final ElementValue reason = map.get("justification");
        if (reason == null || reason.stringifyValue().trim().isEmpty()) {
            BugInstance bugInstance = new BugInstance("FINDBUGS_UNDOCUMENTED_SUPPRESS_WARNINGS",
                    HIGH_PRIORITY).addClass(this);
            if (visitingMethod()) {
                bugInstance.addMethod(this).addSourceLine(this);
            }
            bugReporter.reportBug(bugInstance);
        }
    }

}

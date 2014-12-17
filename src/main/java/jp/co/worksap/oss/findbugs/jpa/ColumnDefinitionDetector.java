package jp.co.worksap.oss.findbugs.jpa;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.ElementValue;

import com.google.common.base.Objects;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import edu.umd.cs.findbugs.internalAnnotations.DottedClassName;

/**
 * <p>A detector which finds columnDefinition property of Column annotation
 * which may break portability.</p>
 *
 * @author Kengo TODA
 */
public class ColumnDefinitionDetector extends AnnotationDetector {

    private final BugReporter reporter;

    public ColumnDefinitionDetector(BugReporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void visitAnnotation(@DottedClassName String annotationClass,
            Map<String, ElementValue> map, boolean runtimeVisible) {
        if (!Objects.equal(annotationClass, "javax.persistence.Column")) {
            return;
        }

        ElementValue columnDefinition = map.get("columnDefinition");
        if (columnDefinition != null) {
            detectExistence(columnDefinition);
        }
    }

    private void detectExistence(@Nonnull ElementValue columnDefinition) {
        String value = columnDefinition.stringifyValue();
        if (!value.isEmpty()) {
            reporter.reportBug(new BugInstance(this, "USE_COLUMN_DEFINITION", NORMAL_PRIORITY).addClass(this).addField(this));
        }
    }

}

package jp.co.worksap.oss.findbugs.jpa;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.Method;
import org.apache.commons.lang.IllegalClassException;

import com.google.common.base.Objects;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import edu.umd.cs.findbugs.internalAnnotations.DottedClassName;

/**
 * <p>Detect column which has too long name. Note that {@code @Column} annotation should annotate FIELD.
 * Currently we do not support annotated METHOD.
 *
 * @author Kengo TODA
 */
public class LongColumnNameDetector extends AnnotationDetector {
    /**
     * <p>Oracle database limits the length of column name, and max length is {@code 30} bytes.
     *
     * @see http://docs.oracle.com/cd/B19306_01/server.102/b14200/sql_elements008.htm
     * @see http://stackoverflow.com/questions/1378133/why-are-oracle-table-column-index-names-limited-to-30-characters
     */
    private static final int MAX_TABLE_LENGTH = 30;

    private final BugReporter bugReporter;

    public LongColumnNameDetector(@Nonnull BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitAnnotation(@DottedClassName String annotationClass,
            Map<String, ElementValue> map, boolean runtimeVisible) {
        if (!Objects.equal(annotationClass, "javax.persistence.Column")) {
            return;
        }
        ElementValue specifiedName = map.get("name");
        final String columnName;
        if (specifiedName != null) {
            columnName = specifiedName.stringifyValue();
        } else if (visitingField()){
            columnName = getFieldName();
        } else if (visitingMethod()) {
            Method targetMethod = getMethod();
            columnName = VisitedFieldFinder.findFieldWhichisVisitedInVisitingMethod(this);
            if (columnName == null) {
                throw new IllegalClassException(String.format(
                        "Method which is annotated with @Column should access to field, but %s#%s does not access.",
                        getClassContext().getClassDescriptor().getClassName().replace('/', '.'),
                        targetMethod.getName()));
            }
        } else {
            throw new IllegalClassException("@Column should annotate method or field.");
        }
        detectLongName(columnName);
    }

    private void detectLongName(@Nonnull String tableName) {
        if (tableName.length() > MAX_TABLE_LENGTH) {
            bugReporter.reportBug(new BugInstance(this, "LONG_COLUMN_NAME",
                    HIGH_PRIORITY).addClass(this));
        }
    }
}

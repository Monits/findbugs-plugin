package jp.co.worksap.oss.findbugs.jpa;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.ElementValue;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import edu.umd.cs.findbugs.internalAnnotations.DottedClassName;
import edu.umd.cs.findbugs.internalAnnotations.SlashedClassName;

public class LongTableNameDetector extends AnnotationDetector {
    /**
     * <p>Oracle database limits the length of table name, and max length is {@code 30} bytes.
     *
     * @see http://docs.oracle.com/cd/B19306_01/server.102/b14200/sql_elements008.htm
     * @see http://stackoverflow.com/questions/1378133/why-are-oracle-table-column-index-names-limited-to-30-characters
     */
    private static final int MAX_TABLE_LENGTH = 30;

    private final BugReporter bugReporter;

    public LongTableNameDetector(@Nonnull BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitAnnotation(@DottedClassName String annotationClass,
            Map<String, ElementValue> map, boolean runtimeVisible) {
        if (!Objects.equal(annotationClass, "javax.persistence.Entity")) {
            return;
        }
        ElementValue specifiedName = map.get("name");
        if (specifiedName != null) {
            detectLongName(specifiedName.stringifyValue());
        } else {
            String entityClassName = trimPackage(getClassName());
            detectLongName(entityClassName);
        }
    }

    @VisibleForTesting
    @Nonnull String trimPackage(@Nonnull @SlashedClassName String className) {
        int index = className.lastIndexOf('/');
        if (index < 0) {
            return className;
        } else {
            return className.substring(index + 1);
        }
    }

    private void detectLongName(@Nonnull String tableName) {
        if (tableName.length() > MAX_TABLE_LENGTH) {
            bugReporter.reportBug(new BugInstance(this, "LONG_TABLE_NAME",
                    HIGH_PRIORITY).addClass(this));
        }
    }
}

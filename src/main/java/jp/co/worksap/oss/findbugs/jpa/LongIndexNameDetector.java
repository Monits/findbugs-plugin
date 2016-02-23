package jp.co.worksap.oss.findbugs.jpa;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.ElementValue;

import com.google.common.base.Objects;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import edu.umd.cs.findbugs.internalAnnotations.DottedClassName;

public class LongIndexNameDetector extends AnnotationDetector {
    /**
     * <p>Oracle database limits the length of index name, and max length is {@code 30} bytes.
     *
     * @see http://docs.oracle.com/cd/B19306_01/server.102/b14200/sql_elements008.htm
     * @see http://stackoverflow.com/questions/1378133/why-are-oracle-table-column-index-names-limited-to-30-characters
     */
    private static final int MAX_INDEX_LENGTH = 30;
    private static final String PARAMETER_NAME_OF_HIBERNATE = "name";
    private static final String PARAMETER_NAME_OF_OPENJPA = "name";
    private final BugReporter bugReporter;

    public LongIndexNameDetector(@Nonnull BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitAnnotation(@DottedClassName String annotationClass,
            Map<String, ElementValue> map, boolean runtimeVisible) {
        if (visitingHibernateAnnotation(annotationClass)) {
            detectLongName(map, PARAMETER_NAME_OF_HIBERNATE);
        } else if (visitingOpenJPAAnnotation(annotationClass)) {
            detectLongName(map, PARAMETER_NAME_OF_OPENJPA);
        }
    }

    private boolean visitingOpenJPAAnnotation(
            @Nonnull @DottedClassName String annotationClass) {
        return Objects.equal(annotationClass, "org.apache.openjpa.persistence.jdbc.Index");
    }

    private boolean visitingHibernateAnnotation(
            @Nonnull @DottedClassName String annotationClass) {
        return Objects.equal(annotationClass, "org.hibernate.annotations.Index");
    }

    private void detectLongName(@Nonnull final Map<String, ElementValue> map,
            @Nonnull final String parameterName) {
        final ElementValue indexName = map.get(parameterName);
        if (indexName != null
                && indexName.stringifyValue().length() > MAX_INDEX_LENGTH) {
            bugReporter.reportBug(new BugInstance(this, "LONG_INDEX_NAME",
                    HIGH_PRIORITY).addClass(this).addField(this));
        }
    }
}

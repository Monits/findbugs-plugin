package jp.co.worksap.oss.findbugs.jpa;

import java.util.List;
import java.util.Map;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.generic.Type;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import edu.umd.cs.findbugs.internalAnnotations.DottedClassName;

abstract class AbstractColumnDetector extends AnnotationDetector {
    private final BugReporter bugReporter;

    /**
     * Creates a new AbstractColumnDetector.
     * @param bugReporter the Bug Reporter to use.
     */
    AbstractColumnDetector(@Nonnull final BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Nonnull
    @CheckReturnValue
    protected final BugReporter getBugReporter() {
    	return bugReporter;
    }

    @Override
    public final void visitAnnotation(@DottedClassName String annotationClass,
            Map<String, ElementValue> map, boolean runtimeVisible) {
        if (!Objects.equal(annotationClass, "javax.persistence.Column")) {
            return;
        }

        Type columnType = findVisitingColumnType();
        verifyColumn(columnType, map);
    }

    protected boolean isVisitingLob() {
        List<FieldOrMethod> targetListToSearch = Lists.newArrayList();
        if (visitingField()) {
            targetListToSearch.add(getField());
        } else if (visitingMethod()) {
            targetListToSearch.add(getMethod());
            targetListToSearch.add(findFieldInVisitingMethod());
        } else {
            throw new IllegalStateException("@Column should annotate field or method.");
        }

        for (FieldOrMethod targetToSearch : targetListToSearch) {
            for (AnnotationEntry annotation : targetToSearch.getAnnotationEntries()) {
                if (!Objects.equal(annotation.getAnnotationType(), "Ljavax/persistence/Lob;")) {
                    continue;
                }
                return true;
            }
        }

        return false;
    }

    private @Nonnull Type findVisitingColumnType() {
        final Type columnType;
        if (visitingField()) {
            columnType = getField().getType();
        } else if (visitingMethod()) {
            Field visitingField = findFieldInVisitingMethod();
            columnType = visitingField.getType();
        } else {
            throw new IllegalStateException("@Column should annotate field or method.");
        }
        return columnType;
    }

    @Nonnull
    private Field findFieldInVisitingMethod() {
        String fieldName = VisitedFieldFinder.findFieldWhichisVisitedInVisitingMethod(this);
        Field visitingField = null;
        for (Field field : getThisClass().getFields()) {
            if (Objects.equal(field.getName(), fieldName)) {
                visitingField = field;
                break;
            }
        }
        if (visitingField == null) {
            throw new IllegalStateException("Cannot find field which named as " + fieldName + ".");
        }
        return visitingField;
    }

    protected abstract void verifyColumn(@Nonnull Type columnType, @Nonnull Map<String, ElementValue> elements);

}

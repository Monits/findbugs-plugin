package jp.co.worksap.oss.findbugs.jpa;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;

public class NullablePrimitiveDetector extends AbstractColumnDetector {

    public NullablePrimitiveDetector(@Nonnull final BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected void verifyColumn(Type columnType,
            Map<String, ElementValue> elements) {
        if (! isPrimitive(columnType)) {
            return;
        }

        boolean isNullableColumn = detectNullability(elements);
        if (isNullableColumn) {
            reportNullablePrimitive(columnType);
        }
    }

	private void reportNullablePrimitive(@Nonnull final Type columnType) {
		BugInstance bug = new BugInstance(this, "NULLABLE_PRIMITIVE", NORMAL_PRIORITY)
				.addClass(this);
		if (visitingMethod()) {
			bug.addMethod(this);
		} else if (visitingField()) {
			bug.addField(this);
		}

		getBugReporter().reportBug(bug);
	}

	private boolean detectNullability(@Nonnull final Map<String, ElementValue> elements) {
		if (! elements.containsKey("nullable")) {
			// in JPA 1.0 specification, default value of 'nullable' parameter is true
			// note that this case will be reported by ImplicitNullnessDetector.
		    return true;
		}

		String nullability = elements.get("nullable").stringifyValue();
		return "true".equals(nullability);
	}

    /**
     * Checks whether a given type is a primitive value or not.
     *
     * @param columnType the type of the column.
     * @return true if column type is primitive value (not reference type).
     */
	private boolean isPrimitive(@Nonnull final Type columnType) {
		return ! (columnType instanceof ObjectType); // looks bad, but simple way to check primitive or not.
	}

}

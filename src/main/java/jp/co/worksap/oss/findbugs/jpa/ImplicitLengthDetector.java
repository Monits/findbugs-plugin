package jp.co.worksap.oss.findbugs.jpa;

import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.generic.Type;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;

public class ImplicitLengthDetector extends AbstractColumnDetector {
    /**
     * @see http://docs.oracle.com/cd/B28359_01/server.111/b28320/limits001.htm
     */
    private static final int MAX_LENGTH_OF_ORACLE_VARCHAR = 4000;
    /**
     * @see http://www-01.ibm.com/support/knowledgecenter/SSEPEK_10.0.0/com.ibm.db2z10.doc.intro/src/tpc/db2z_stringdatatypes.htm
     */
    private static final int MAX_LENGTH_OF_DB2_VARCHAR = 32704;

    private static final int MAX_LENGTH_OF_VARCHAR = Math.min(MAX_LENGTH_OF_ORACLE_VARCHAR, MAX_LENGTH_OF_DB2_VARCHAR);

    public ImplicitLengthDetector(final BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected void verifyColumn(Type columnType,
            Map<String, ElementValue> elements) {
        if (! isTarget(columnType)) {
            return;
        }

        if (! elements.containsKey("length")) {
            BugInstance bug = new BugInstance(this, "IMPLICIT_LENGTH", HIGH_PRIORITY).addClass(this);
            if (visitingMethod()) {
                bug.addMethod(this);
            } else if (visitingField()) {
            	bug.addField(this);
            }
            getBugReporter().reportBug(bug);
        } else {
            ElementValue value = elements.get("length");
            int lengthValue = Integer.parseInt(value.stringifyValue());

            if (lengthValue <= 0) {
                reportIllegalLength(lengthValue);
            } else if (MAX_LENGTH_OF_VARCHAR < lengthValue && !isVisitingLob()) {
                reportIllegalLength(lengthValue);
            }
        }
    }

    private void reportIllegalLength(int lengthValue) {
        BugInstance bug = new BugInstance(this, "ILLEGAL_LENGTH", HIGH_PRIORITY).addClass(this);
        if (visitingMethod()) {
            bug.addMethod(this);
        } else if (visitingField()) {
        	bug.addField(this);
        }
        getBugReporter().reportBug(bug);
    }

    /**
     * @return true if column type requires length property.
     */
    private boolean isTarget(@Nonnull Type columnType) {
        return Type.STRING.equals(columnType) || Type.STRINGBUFFER.equals(columnType);
    }

}

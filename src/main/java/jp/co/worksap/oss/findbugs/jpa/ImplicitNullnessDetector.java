package jp.co.worksap.oss.findbugs.jpa;

import java.util.Map;

import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.generic.Type;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;

public class ImplicitNullnessDetector extends AbstractColumnDetector {

    public ImplicitNullnessDetector(BugReporter bugReporter) {
        super(bugReporter);
    }

    @Override
    protected void verifyColumn(Type columnType,
            Map<String, ElementValue> elements) {
        if (! elements.containsKey("nullable")) {
            BugInstance bug = new BugInstance(this, "IMPLICIT_NULLNESS", HIGH_PRIORITY).addClass(this);
            if (visitingMethod()) {
                bug.addMethod(this);
            } else if (visitingField()) {
             	bug.addField(this);
            }
            getBugReporter().reportBug(bug);
        }
    }
}

package samples.findbugs;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class UndocumentedSuppressFBWarnings {
    @SuppressFBWarnings("SF_SWITCH_NO_DEFAULT")
    public void method() {
    }
}

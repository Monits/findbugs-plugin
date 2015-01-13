package samples.junit;

import org.junit.Ignore;

public class IgnoreMethodWithEmptyExplanation {
    @Ignore("")
    public void method() {
    }
}

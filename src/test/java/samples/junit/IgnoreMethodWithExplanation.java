package samples.junit;

import org.junit.Ignore;

public class IgnoreMethodWithExplanation {
    @Ignore("Good explanation to tell the reason.")
    public void method() {
    }
}

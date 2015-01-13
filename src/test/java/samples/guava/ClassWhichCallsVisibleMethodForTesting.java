package samples.guava;

public class ClassWhichCallsVisibleMethodForTesting {
    public void method() {
        new MethodWithVisibleForTesting().method();
    }
}

package samples.guava;

public class ClassWhichCallsNormalMethod {
    public void method() {
        new MethodWithoutVisibleForTesting().method();
    }

    public void anotherMethod() {
        System.out.println("this method invoking has no problem, because package isn't same.");
        method();
    }
}

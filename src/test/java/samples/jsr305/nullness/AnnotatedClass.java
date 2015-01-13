package samples.jsr305.nullness;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class AnnotatedClass {
    public void method(Object value) {}
}

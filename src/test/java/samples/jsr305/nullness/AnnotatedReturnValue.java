package samples.jsr305.nullness;

import javax.annotation.Nullable;

public class AnnotatedReturnValue {
    @Nullable
    public Object method() {
        return null;
    }
}

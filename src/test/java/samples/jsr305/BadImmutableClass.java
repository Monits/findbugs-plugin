package samples.jsr305;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
@Immutable // marked as immutable, but field is not final
public class BadImmutableClass {
    public String value;
}

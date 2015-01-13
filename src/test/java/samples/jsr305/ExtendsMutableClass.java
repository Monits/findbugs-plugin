package samples.jsr305;

import javax.annotation.concurrent.Immutable;


@Immutable // this class looks immutable, but it extends mutable class
public final class ExtendsMutableClass extends BadImmutableClass {
    private final String string;

    public ExtendsMutableClass(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}

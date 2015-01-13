package samples.jpa;

import javax.persistence.Column;

public class GetterWithTooLongLength {
    private String name;

    @Column(length = 10000)
    public String getName() {
        return name;
    }
}

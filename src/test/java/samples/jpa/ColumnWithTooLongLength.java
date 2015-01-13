package samples.jpa;

import javax.persistence.Column;

public class ColumnWithTooLongLength {
    @Column(length = 10000)
    private String name;

    public String getName() {
        return name;
    }
}

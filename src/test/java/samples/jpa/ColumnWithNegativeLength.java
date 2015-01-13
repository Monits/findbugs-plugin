package samples.jpa;

import javax.persistence.Column;

public class ColumnWithNegativeLength {
    @Column(length = -1)
    private String name;

    public String getName() {
        return name;
    }
}

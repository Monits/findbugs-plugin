package samples.jpa;

import javax.persistence.Column;

public class ColumnWithLength {
    @Column(length = 100)
    private String name;

    public String getName() {
        return name;
    }
}

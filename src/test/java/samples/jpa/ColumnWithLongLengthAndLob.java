package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Lob;

public class ColumnWithLongLengthAndLob {
    @Lob
    @Column(length = 10000)
    private String name;

    public String getName() {
        return name;
    }
}

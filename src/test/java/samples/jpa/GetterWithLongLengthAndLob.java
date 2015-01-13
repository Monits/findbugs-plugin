package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Lob;

public class GetterWithLongLengthAndLob {
    private String name;

    @Lob
    @Column(length = 10000)
    public String getName() {
        return name;
    }
}

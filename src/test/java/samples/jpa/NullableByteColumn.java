package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NullableByteColumn {
    @Column(nullable = true)
    private byte byteValue;
}

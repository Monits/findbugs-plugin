package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NullableFloatColumn {
    @Column(nullable = true)
    private float floatValue;
}

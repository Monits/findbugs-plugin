package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NullableShortColumn {
    @Column(nullable = true)
    private short shortValue;
}

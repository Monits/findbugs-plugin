package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NullableBooleanColumn {
    @Column(nullable = true)
    private boolean booleanValue;
}

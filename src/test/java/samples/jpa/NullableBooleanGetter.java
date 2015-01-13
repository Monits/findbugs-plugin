package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NullableBooleanGetter {
    private boolean booleanValue;

    @Column(nullable = true)
    public boolean isBooleanValue() {
        return booleanValue;
    }
}

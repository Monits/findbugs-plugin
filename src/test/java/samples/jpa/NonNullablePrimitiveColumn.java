package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NonNullablePrimitiveColumn {
    @Column(nullable = false)
    private boolean booleanValue;

    @Column(nullable = false)
    private byte byteValue;

    @Column(nullable = false)
    private short shortValue;

    @Column(nullable = false)
    private int intValue;

    @Column(nullable = false)
    private long longValue;

    @Column(nullable = false)
    private float floatValue;

    @Column(nullable = false)
    private double doubleValue;
}

package samples.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LongTableNameWithoutAnnotationParameter {

    @Id
    private long id;

    public LongTableNameWithoutAnnotationParameter(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}

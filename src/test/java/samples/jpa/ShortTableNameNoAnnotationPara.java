package samples.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ShortTableNameNoAnnotationPara {

    @Id
    private long id;

    public ShortTableNameNoAnnotationPara(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}

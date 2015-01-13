package samples.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="table_name_longer_than_30_bytes")
public class LongTableName {

    @Id
    private long id;

    public LongTableName(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}

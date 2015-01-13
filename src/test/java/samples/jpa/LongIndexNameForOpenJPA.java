package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class LongIndexNameForOpenJPA {
    @Id
    private long id;

    @Column(nullable = true)
    @Index(name = "index_name_longer_than_30_bytes")
    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

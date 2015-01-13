package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.openjpa.persistence.jdbc.Index;

@Entity
public class ShortIndexNameForOpenJPA {
    @Id
    private long id;

    @Column(nullable = true)
    @Index(name = "idx_name_shorter_than_31_bytes")
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

package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ShortColumnNameWithoutAnnotationParameter {
    @Id
    private long id;

    @Column
    private String shortColumnNameNoAnnotationPrm;

    private String another;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShortColumnNameNoAnnotationPrm() {
        return shortColumnNameNoAnnotationPrm;
    }

    public void setShortColumnNameNoAnnotationPrm(
            String shortColumnNameNoAnnotationPrm) {
        this.shortColumnNameNoAnnotationPrm = shortColumnNameNoAnnotationPrm;
    }

    @Column
    public String getAnother() {
        return another;
    }

    public void setAnother(String another) {
        this.another = another;
    }
}

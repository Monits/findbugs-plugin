package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LongColumnNameWithoutAnnotationParameter {
    @Id
    private long id;

    @Column
    private String longColumnNameWithoutAnnotationParameter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLongColumnNameWithoutAnnotationParameter() {
        return longColumnNameWithoutAnnotationParameter;
    }

    public void setLongColumnNameWithoutAnnotationParameter(
            String longColumnNameWithoutAnnotationParameter) {
        this.longColumnNameWithoutAnnotationParameter = longColumnNameWithoutAnnotationParameter;
    }

}

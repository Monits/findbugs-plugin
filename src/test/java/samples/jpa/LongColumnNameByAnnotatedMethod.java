package samples.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LongColumnNameByAnnotatedMethod {
    @Id
    private long id;

    private String longColumnNameByAnnotatedMethod;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getSomething() { // Plugin has to alert even if method name is short
        return longColumnNameByAnnotatedMethod;
    }

    public void setLongColumnNameByAnnotatedMethod(
            String longColumnNameByAnnotatedMethod) {
        this.longColumnNameByAnnotatedMethod = longColumnNameByAnnotatedMethod;
    }

}

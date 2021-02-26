package victor.training.ddd.customer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Site {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    protected Site() {} // hibernate
    public Site(String name) {
        this.name = name;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}

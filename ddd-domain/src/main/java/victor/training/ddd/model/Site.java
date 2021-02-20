package victor.training.ddd.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Site {
    @Id
    @GeneratedValue
    @Getter
    private Long id;

    @Getter
    private String name;

    protected Site() {} // hibernate
    public Site(String name) {
        this.name = name;
    }
}

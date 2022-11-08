package victor.training.ddd.agile.domain.model;

import victor.training.ddd.agile.domain.model.Sprint.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

@Entity
public class BacklogItem {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Product product;
    @NotNull
    private String title; // "b"
    private String description; // ""

    @Version
    private Long version;

    protected BacklogItem() {
    }

    public BacklogItem(Product product, String title, String description) {
        this.product = product;
        this.title = title;
        this.description = description;
    }


    public BacklogItem setId(Long id) {
        this.id = id;
        return this;
    }

    public BacklogItem setDescription(String description) {
        this.description = description;
        return this;
    }

    public BacklogItem setProduct(Product product) {
        this.product = product;
        return this;
    }

    public BacklogItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public BacklogItem setVersion(Long version) {
        this.version = version;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public @NotNull String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getVersion() {
        return version;
    }

}

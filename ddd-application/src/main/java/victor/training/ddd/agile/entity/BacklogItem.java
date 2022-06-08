package victor.training.ddd.agile.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


/// JOIN inheritance strategy is the most inefficient from performance. Vlad Mihalcea and Thorben Janssen both say never to use it.

//abstract class BI {}
//class PI extends BI {}
//class SI extends BI {}
//
//new PI();
//

@Entity
public class BacklogItem {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Product product;

    @NotNull
    private String title;
    private String description;
    // once you estimate an item, title and descr cannot change
    private boolean underDevelopment;
    @Version // optimistic locking to detect cases when the same item is changed in parallel by 2 users.
    private Long version;

    public BacklogItem() {
    }

    public Long getId() {
        return this.id;
    }

    public Product getProduct() {
        return this.product;
    }

    public @NotNull String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isUnderDevelopment() {
        return this.underDevelopment;
    }

    public Long getVersion() {
        return this.version;
    }

    public BacklogItem setId(Long id) {
        this.id = id;
        return this;
    }

    public BacklogItem setProduct(Product product) {
        this.product = product;
        return this;
    }


    public BacklogItem setTitle(@NotNull String title) {
        if(underDevelopment) throw new IllegalArgumentException("Cannot edit");
        this.title = title;
        return this;
    }

    public BacklogItem setDescription(String description) {
        if(underDevelopment) throw new IllegalArgumentException("Cannot edit");
        this.description = description;
        return this;
    }

    public BacklogItem setVersion(Long version) {
        this.version = version;
        return this;
    }

    public void startDevelopment() {
        underDevelopment = true;
    }
    public void endDevelopment() {
        underDevelopment = false;
    }
}

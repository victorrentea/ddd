package victor.training.ddd.agile.entity;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable // this is how we implement Value Objects that we want to persist with Hibernate
public class ProductOwner {
    private String ownerEmail;
    private String ownerName;
    private String ownerPhone;

    ProductOwner() {} // for Hibernate
    public ProductOwner(String ownerName, String ownerEmail, String ownerPhone) {
        this.ownerName = Objects.requireNonNull(ownerName);
        this.ownerEmail = Objects.requireNonNull(ownerEmail);
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }
}

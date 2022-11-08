package victor.training.ddd.agile.domain.model;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ProductOwner {

    private String ownerEmail;
    private String ownerName;
    private String ownerPhone;

    protected ProductOwner() {
    }

    public ProductOwner(String ownerEmail, String ownerName, String ownerPhone) {
        this.ownerName = ownerName;
        if (ownerEmail == null && ownerPhone == null) {
            throw new IllegalArgumentException("Not valid!");
        }
        this.ownerEmail = ownerEmail;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductOwner that = (ProductOwner) o;
        return Objects.equals(ownerEmail, that.ownerEmail) && Objects.equals(ownerName, that.ownerName) && Objects.equals(ownerPhone, that.ownerPhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerEmail, ownerName, ownerPhone);
    }

    public ProductOwner withOwnerPhone(String ownerPhone) {
        return this.ownerPhone == ownerPhone ? this : new ProductOwner(this.ownerEmail, this.ownerName, ownerPhone);
    }
}

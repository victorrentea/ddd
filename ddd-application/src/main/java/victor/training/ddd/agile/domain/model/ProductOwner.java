package victor.training.ddd.agile.domain.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.Optional;

@Embeddable // VALUE OBJECT
public class ProductOwner {
   @Column(nullable = false)
   private String name;
   @Column(nullable = false)
   private String email;
//   @Column(name = "OWNER_PHONE")
   private String phone;

   private ProductOwner() {
   }

   public ProductOwner(String email, String name, String phone) {
      this.name = Objects.requireNonNull(name);
      this.email = Objects.requireNonNull(email);
      this.phone = phone;
   }

   public String getName() {
      return name;
   }

   public String getEmail() {
      return email;
   }

   public Optional<String> getPhone() {
      return Optional.ofNullable(phone);
   }
}

package victor.training.ddd.agile.domain.model;

import javax.persistence.Embeddable;

@Embeddable // VALUE OBJECT
public class ProductOwner {
   private String email;
   private String name;
   private String phone;

   private ProductOwner() {
   }

   public ProductOwner(String email, String name, String phone) {
      this.email = email;
      this.name = name;
      this.phone = phone;
   }

   public String getName() {
      return name;
   }

   public String getEmail() {
      return email;
   }

   public String getPhone() {
      return phone;
   }
}

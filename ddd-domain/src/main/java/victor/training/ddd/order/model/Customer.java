package victor.training.ddd.order.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class Customer {
   @Id
   @GeneratedValue
   private Long id;
   private String firstName;
   private String middleName; // optional
   private String lastName;

   private String shippingCity;
   private String shippingPostalCode; // not needed when city = "Bucuresti"
   private String shippingStreetAddress;
   private String shippingAddressLine2;
   private String shippingPhone;

   private String createdByUser;
   private LocalDateTime createdAt;

   private String modifiedByUser;
   private LocalDateTime modifiedAt;

   // 60 more fields! ... because everything is about the customer
}

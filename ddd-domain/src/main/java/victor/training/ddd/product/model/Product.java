package victor.training.ddd.product.model;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Product {
   @Id
   @GeneratedValue
   private Long id;

   private String name; // required

   @Lob
   private String description;

   @Embedded
   private ShippingDetails shippingDetails;

   private boolean returnable;
   private int returnMaxDays; // required

   private String createdByUser;
   private LocalDateTime createdAt;
//   private AuditAction creation;

   private String modifiedByUser;
   private LocalDateTime modifiedAt;
//   private AuditAction modification;

   @Enumerated(EnumType.STRING)
   private ProductCategory category; // required

   @ManyToOne
   private Supplier supplier;

   protected Product() {}

   public Product(String name, ShippingDetails shippingDetails, int returnMaxDays, ProductCategory category) {
      this.name = name;
      this.shippingDetails = shippingDetails;
      this.returnMaxDays = returnMaxDays;
      this.category = category;
   }
}

package victor.training.ddd.order.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class OrderLine {
   @Id
   @GeneratedValue
   private Long id; // no setter!
   private Long supplier;
   @NotNull // hibernate valideaza orice astfel de adnotare la persist/save
   // permite sa opresti date invalide sa ajunga in DB.
   private String productId;
   @NotNull
   private String email;
   @Min(1)
   private BigDecimal itemQuantity = BigDecimal.ONE;
   @NotNull
   private BigDecimal itemPrice;

   public Long getId() {
      return id;
   }


   public Long getSupplier() {
      return supplier;
   }

   public OrderLine setSupplier(Long supplier) {
      this.supplier = supplier;
      return this;
   }

   public String getProductId() {
      return productId;
   }

   public OrderLine setProductId(String productId) {
      this.productId = productId;
      return this;
   }

   public BigDecimal getItemQuantity() {
      return itemQuantity;
   }

   public OrderLine setItemQuantity(BigDecimal itemQuantity) {
      this.itemQuantity = itemQuantity;
      return this;
   }

   public BigDecimal getItemPrice() {
      return itemPrice;
   }

   public OrderLine setItemPrice(BigDecimal itemPrice) {
      this.itemPrice = itemPrice;
      return this;
   }

   public BigDecimal getTotalPrice() {
      return itemPrice.multiply(itemQuantity);
   }
}

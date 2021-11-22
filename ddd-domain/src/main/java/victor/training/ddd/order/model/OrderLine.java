package victor.training.ddd.order.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;

@Entity
public class OrderLine {
   @Id
   @GeneratedValue
   private Long id; // no setter!
   private String productId; // required
   private BigDecimal itemQuantity ;  // required
   private BigDecimal itemPrice; // required
   private Long supplier;

   protected OrderLine() { // just for hibernate 💘
   }
   public OrderLine(String productId, BigDecimal itemQuantity, BigDecimal itemPrice) {
      this.productId = Objects.requireNonNull(productId);
      if (itemQuantity.compareTo(ZERO) < 0) {
         throw new IllegalArgumentException("Quantity must be positive");
      }
      this.itemQuantity = Objects.requireNonNull(itemQuantity);
      if (itemPrice.compareTo(ZERO) < 0) {
         throw new IllegalArgumentException("Price must be positive");
      }
      this.itemPrice = Objects.requireNonNull(itemPrice);
   }


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


   public BigDecimal getItemQuantity() {
      return itemQuantity;
   }

   public BigDecimal getItemPrice() {
      return itemPrice;
   }

   public BigDecimal getTotalPrice() {
      return itemPrice.multiply(itemQuantity);
   }
}

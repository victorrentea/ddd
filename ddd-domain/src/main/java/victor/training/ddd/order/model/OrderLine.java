package victor.training.ddd.order.model;

import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;

@Service
class UndevaIntrunService {
//   private final SupRepo
   public void method(OrderLine line) {

//      line.getSupplierId()
   }

}

@Entity
public class OrderLine {
   @Id
   @GeneratedValue
   private Long id; // no setter!
   private String productId; // required
   private BigDecimal itemQuantity ;  // required
   private BigDecimal itemPrice; // required
//   @ManyToOne(fetch = FetchType.LAZY)
//   private Supplier supplier;

   private Long supplierId; // => daca generezi schema cu Hibernate din @Entity ->
   // nu va mai exista FK intre OrderLIne si Supplier. il pui de mana ! Pastrezi FK aici !

   public Long getSupplierId() {
      return supplierId;
   }

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


//   public Long getSupplier() {
//      return supplier;
//   }
//
//   public OrderLine setSupplier(Long supplier) {
//      this.supplier = supplier;
//      return this;
//   }

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

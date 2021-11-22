package victor.training.ddd.order.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class OrderLine {
   @Id
   @GeneratedValue
   private Long id; // no getter!

   private Long supplier;
   private String productId; // in the DB while you are on the same DB server, recommended to keep the FK between OrderLine -> Product
   private BigDecimal itemQuantity;

   private BigDecimal itemPrice;

   @ManyToOne
   private Order order;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getSupplier() {
      return supplier;
   }

   public void setSupplier(Long supplier) {
      this.supplier = supplier;
   }

   public String getProductId() {
      return productId;
   }

   public void setProductId(String productId) {
      this.productId = productId;
   }

   public BigDecimal getItemQuantity() {
      return itemQuantity;
   }

   public void setItemQuantity(BigDecimal itemQuantity) {
      this.itemQuantity = itemQuantity;
   }

   public BigDecimal getItemPrice() {
      return itemPrice;
   }

   public void setItemPrice(BigDecimal itemPrice) {
      this.itemPrice = itemPrice;
   }

   public Order getOrder() {
      return order;
   }

   void setOrder(Order order) {
      this.order = order;
   }
}

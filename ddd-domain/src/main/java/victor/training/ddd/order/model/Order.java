package victor.training.ddd.order.model;

import victor.training.ddd.customer.model.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS") // SQL keyword collision
public class Order {
   @Id
   @GeneratedValue
   private Long id;

   private BigDecimal totalPrice = BigDecimal.ZERO;


//   @ElementCollection
   @OneToMany
   @JoinColumn // otherwise generates a join table
   private List<OrderLine> orderLines = new ArrayList<>();

   private LocalDateTime paymentTime;
   private boolean shipped;

   // TODO 1 factory method for OrderLines
   // TODO 2 hide OrderLine completely
   public void add(OrderLine orderLine) {
      orderLines.add(orderLine);
      totalPrice = totalPrice.add(orderLine.computePrice());
   }

   // hiding OrderLine child entity
   public void setItemCount(Long productId, int newCount) {
      OrderLine orderLine = orderLines.stream().filter(line -> line.productId().equals(productId)).findFirst().get();
      BigDecimal oldPrice = orderLine.computePrice();
      orderLine.itemCount(newCount);
      BigDecimal newPrice = orderLine.computePrice();
      totalPrice = totalPrice.add(newPrice).subtract(oldPrice);

   }

   public Long id() {
      return id;
   }

   public Order ship() {
      this.shipped = true;
      return this;
   }
   public Order pay() {
      this.paymentTime = LocalDateTime.now();
      return this;
   }
   public boolean payed() {
      return paymentTime != null;
   }
}




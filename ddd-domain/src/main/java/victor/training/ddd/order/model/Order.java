package victor.training.ddd.order.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

// Aggregate si Entitate
//@Entity
public class Order {
//   private ImmutableList<OrderLine> orderLines = new ArrayList<>();
//   @Size(min = 1)
   private List<OrderLine> orderLines = new ArrayList<>();
   private int totalPrice;
   private LocalDateTime dateShipped;

   public Order(List<OrderLine> orderLines) {
      if (orderLines.isEmpty()) {
         throw new IllegalArgumentException("At least one OrderLine is required");
      }
      for (OrderLine orderLine : orderLines) {
         addProduct(orderLine);
      }
   }

   

   public Long getId() {
      return 1L;
   }

   public void setDateShipped(LocalDateTime dateShipped) {
      if (dateShipped.isBefore(LocalDateTime.now().minusMonths(1))) {
         throw new IllegalArgumentException("Data e prea in trecut");
      }
      this.dateShipped = dateShipped;
   }

   public List<OrderLine> getOrderLines() {
      return unmodifiableList(orderLines);
   }
   public Integer getTotalPrice() {
      return totalPrice;
   }
   public void addProduct(OrderLine orderLine) {
      // invariant: sum(orderLine.product.price * orderLine.count)=order.totalPrice
      orderLines.add(orderLine);
      totalPrice += orderLine.getPrice();
   }
   public void removeProduct(OrderLine orderLine) {
      // invariant: sum(orderLine.product.price * orderLine.count)=order.totalPrice
      if (orderLines.size() == 1) {
         throw new IllegalArgumentException("The order must have at least one OrderLine");
      }
      orderLines.remove(orderLine);
      totalPrice -= orderLine.getPrice();
   }
}

class OrderFactory {

}


class OrderService {
   public  void main(String[] args) {
      // Logica central . eg placeOrder
      Product product = new Product(12);

      OrderLine orderLine = new OrderLine(product, 1);
      Order order = new Order(asList(orderLine));

      validator.validate(order);

   }
   @Autowired
      Validator validator;
// prod
   public void cancelOrder(Order order) {
//      http.send (order.getId());

   }
}
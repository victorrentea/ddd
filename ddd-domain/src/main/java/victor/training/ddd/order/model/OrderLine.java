package victor.training.ddd.order.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class OrderLine {
   @Id
   @GeneratedValue
   private Long id;
   private String productId;
   private Integer itemQuantity;
   private Integer itemPrice;
   @ManyToOne
   private Order order;
   @ManyToOne
   private Supplier supplier;

   public OrderLine() { // Hibernate 💘
   }

}

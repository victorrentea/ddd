package victor.training.ddd.facade.dto;

import victor.training.ddd.order.model.OrderLine;
import victor.training.ddd.order.model.SupplierId;

import java.math.BigDecimal;

public class OrderLineDto {
   public String supplierId;
   public String productId;
   public BigDecimal itemPrice;
   public BigDecimal itemQuantity;

   public OrderLine toEntity() {
      OrderLine orderLine = new OrderLine(productId, itemPrice, itemQuantity);
      orderLine.supplierId(new SupplierId(supplierId));
      return orderLine;
   }
}

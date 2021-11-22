package victor.training.ddd.order.model;

import java.math.BigDecimal;

public class TestDummyData {

// pentru teste
   public static OrderLine anOrderLine() {
      return new OrderLine("::productId::", BigDecimal.ONE, BigDecimal.TEN);
   }
}

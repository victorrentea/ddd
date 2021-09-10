package victor.training.ddd.order.model;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

   @Test
   void cancelOrder() {
      Order order = DummyData.aValidOrder();
//      order.setId(12L);

      new OrderService().cancelOrder(order);

//      verify(ca s-a trimis HTTP/mesaje)

   }

}

class DummyData {
   public static Order aValidOrder() {
      return new Order(asList(new OrderLine(new Product(12), 2)));
   }

}
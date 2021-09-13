package victor.training.ddd.order.model;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;

class OrderServiceTest {

   @Test
   void cancelOrder() {
      Order order = DummyData.aValidOrder();
//      order.setId(12L);

//      new OrderService().applyCoupon(order, "12");

//      verify(ca s-a trimis HTTP/mesaje)

   }

}

class DummyData {
   public static Order aValidOrder() {
      return new Order(asList(new OrderLine(null, 2)));
   }

}
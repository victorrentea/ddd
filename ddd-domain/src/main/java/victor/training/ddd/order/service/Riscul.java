package victor.training.ddd.order.service;

import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class Riscul {
   {
      Order order = new Order();
      OrderLine orderLine = new OrderLine("pid", BigDecimal.valueOf(6), BigDecimal.valueOf(10));

      // grije sa le setez mereu pe amandoua!
      order.addLine(orderLine);


      //  order.getLines().add(orderLine); // runtime exception.
//      orderLine.setOrder(order); // nu compileaza

      EntityManager em = null;
      em.persist(order);
      em.persist(orderLine);

   }
}

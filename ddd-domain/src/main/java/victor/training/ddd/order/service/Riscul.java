package victor.training.ddd.order.service;

import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;

import javax.persistence.EntityManager;

public class Riscul {
   {
      Order order = new Order();
      OrderLine orderLine = new OrderLine();

      // grija sa le setez mereu pe amandoua!
      order.addLine(orderLine);


      //  order.getLines().add(orderLine); // runtime exception.
      orderLine.setOrder(order);

      EntityManager em = null;
      em.persist(order);
      em.persist(orderLine);

   }
}

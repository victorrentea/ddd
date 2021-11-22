package victor.training.ddd.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;
import victor.training.ddd.order.repo.OrderRepo;

import javax.persistence.EntityManager;
import javax.validation.Validator;
import java.math.BigDecimal;

//@Interceptor   javax.inject.@Interceptor  (CDI)

public class CumValidezi {

   @Autowired
   Validator validator;
   @Autowired
   EntityManager em;
   @Autowired
   OrderRepo orderRepo;


   public void method(@Validated Order order) { // spring proxy care valideaza param inainte sa-ti cheme functia
//      ValidatorFactory


      OrderLine line = new OrderLine();

      line.setItemQuantity(BigDecimal.valueOf(-1));

      order.addLine(line);

//      sendEmail / send mesaje pe coada ca order are pretul :
      order.getTotalPrice();  // -3


      validator.validate(order);// programatic < riscant.




      em.persist(order); // EM EJB automat
      orderRepo.save(order); // spring data automat
   }

}

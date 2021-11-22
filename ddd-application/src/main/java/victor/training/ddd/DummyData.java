package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;
import victor.training.ddd.order.model.SupplierId;
import victor.training.ddd.order.repo.OrderRepo;
import victor.training.ddd.order.service.OrderService;

import java.math.BigDecimal;

//@Profile("insertDummyData") // in real project
@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

   private final OrderRepo orderRepo;
   private final OrderService orderService;

   @Override
   public void run(String... args) throws Exception {

      X.oldMethod();

//      Order order = new Order().setClientId("clientId");
//      order.add(new OrderLine("Pfeiser", BigDecimal.TEN, BigDecimal.ONE).supplierId(new SupplierId("eMAG")));
//      orderRepo.save(order);
//      System.out.println(orderRepo.findAll());
//      orderService.suppliersOrdersData(order);



   }

}

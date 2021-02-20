package victor.training.ddd.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import victor.training.ddd.facade.OrderFacade;
import victor.training.ddd.model.Order;
import victor.training.ddd.repo.OrderRepo;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("db-mem")
class OrderFacadeTest {
   @Autowired
   private OrderFacade orderFacade;
   @Autowired
   private OrderRepo repo;

   @Test
   void findPurpleOrders() {
      Long orderInvalid = repo.save(new Order().ship()).id();
      repo.save(new Order().pay().ship());
      repo.save(new Order());

      assertThat(orderFacade.findPurpleOrders()).map(Order::id).containsExactly(orderInvalid);
   }
}
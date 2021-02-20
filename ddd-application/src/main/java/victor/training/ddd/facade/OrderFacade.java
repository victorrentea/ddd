package victor.training.ddd.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.ddd.model.Order;
import victor.training.ddd.repo.OrderRepo;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.not;
import static victor.training.ddd.repo.OrderSpec.payed;
import static victor.training.ddd.repo.OrderSpec.shipped;

@Slf4j
@Facade
@RequiredArgsConstructor
public class OrderFacade {
   private final OrderRepo orderRepo;

   public List<Order> findPurpleOrders() { // special DOmain term for shipped but not paied orders
      return orderRepo.findAll(shipped().and(not(payed())));
   }
}

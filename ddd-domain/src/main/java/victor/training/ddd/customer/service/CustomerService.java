package victor.training.ddd.customer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.ddd.customer.repo.CustomerRepo;
import victor.training.ddd.order.service.OrderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
   private final CustomerRepo customerRepo;
   private final OrderService orderService;

   public void create() {

   }

}

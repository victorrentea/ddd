package victor.training.ddd.service.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.ddd.MyException;
import victor.training.ddd.MyException.ErrorCode;
import victor.training.ddd.repo.CustomerRepo;
import victor.training.ddd.repo.OrderRepo;
import victor.training.ddd.service.order.OrderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
   private final CustomerRepo customerRepo;
   private final OrderService orderService;

   public void create() {

   }

}

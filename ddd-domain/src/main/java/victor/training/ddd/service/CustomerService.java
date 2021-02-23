package victor.training.ddd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.ddd.MyException;
import victor.training.ddd.MyException.ErrorCode;
import victor.training.ddd.repo.CustomerRepo;
import victor.training.ddd.repo.OrderRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
   private final CustomerRepo customerRepo;

   public void create() {

   }

}

package victor.training.ddd.service.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import victor.training.ddd.repo.CustomerRepo;
import victor.training.ddd.service.events.CustomerCreatedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
   private final CustomerRepo customerRepo;

   private final ApplicationEventPublisher publisher;

   public void create() {
      publisher.publishEvent(new CustomerCreatedEvent(13));
   }

}

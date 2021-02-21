package victor.training.ddd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.ddd.events.CustomerAddressChanged;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotingService {
//   @EventListener
   // or
   @Async
   @TransactionalEventListener
   @Transactional
   public void handleCustomerAddressChanged(CustomerAddressChanged event) {
      log.info("Got Address changed: " + event);
   }
}

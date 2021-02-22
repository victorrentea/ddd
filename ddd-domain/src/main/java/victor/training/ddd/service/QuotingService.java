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
//   @EventListener // by default Spring RUNs this sync and in the same TX as from where you publish it
   // or
//   @Async
   @TransactionalEventListener
   @Transactional
   public void handleCustomerAddressChanged(CustomerAddressChanged event) {
      // Running in a second transaction AFTER the one that changed the Aggregate
      log.info("Got Address changed: " + event);
      // imagine load quotation for that customer
      // requote
      // persist new quote
      // send message over to another system to notify customer

   }
}

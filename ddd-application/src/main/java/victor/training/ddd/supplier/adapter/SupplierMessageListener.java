package victor.training.ddd.supplier.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import victor.training.ddd.supplier.service.OrderConfirmedEventHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class SupplierMessageListener {
   private final OrderConfirmedEventHandler orderConfirmedEventHandler;
   @ServiceActivator(inputChannel = "ordersConfirmedIn")
   public void method(Long orderId) {
      log.info("Received message on queue + " +orderId);
      orderConfirmedEventHandler.suppliersOrdersData(orderId);
   }
}

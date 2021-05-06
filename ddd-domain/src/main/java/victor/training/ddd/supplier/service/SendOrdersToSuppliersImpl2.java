package victor.training.ddd.supplier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.ddd.common.events.OrderConfirmedEvent;
import victor.training.ddd.order.model.Order;

@Profile("client2")
@Service
@RequiredArgsConstructor
public class SendOrdersToSuppliersImpl2 implements OrderConfirmedEventHandler {
   private final SupplierService supplierService;

   @Override
   public void suppliersOrdersData(Long orderId) {

   }
}

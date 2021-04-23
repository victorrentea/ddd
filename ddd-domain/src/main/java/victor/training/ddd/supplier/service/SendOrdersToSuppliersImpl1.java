package victor.training.ddd.supplier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.ddd.common.events.OrderConfirmedEvent;
import victor.training.ddd.supplier.model.ProductWithQuantity;
import victor.training.ddd.supplier.model.OrderLineVO;
import victor.training.ddd.supplier.model.OrderVO;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@Profile("client1")
@RequiredArgsConstructor
public class SendOrdersToSuppliersImpl1  implements OrderConfirmedEventHandler {
   private final SupplierService supplierService;
   private final OrderServiceClient orderServiceClient;

   @Override
   @EventListener
   public void suppliersOrdersData(OrderConfirmedEvent orderConfirmedEvent) {

      OrderVO order = orderServiceClient.getOrder(orderConfirmedEvent.getId());

      Map<Long, List<ProductWithQuantity>> result = order.orderLines().stream()
          .collect(groupingBy(OrderLineVO::supplierId,
              mapping(line -> new ProductWithQuantity(getProduct(line), line.itemQuantity()), toList())));
      supplierService.sendOrders(result);
   }
   private String getProduct(OrderLineVO line) {
      return "TODO";
   }
}

package victor.training.ddd.supplier.service;

import victor.training.ddd.common.events.OrderConfirmedEvent;
import victor.training.ddd.order.model.Order;

import java.time.LocalDateTime;

public interface OrderConfirmedEventHandler {
   void suppliersOrdersData(OrderConfirmedEvent order);
}

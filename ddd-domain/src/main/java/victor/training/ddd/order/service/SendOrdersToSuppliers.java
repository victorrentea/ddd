package victor.training.ddd.order.service;

import victor.training.ddd.order.model.Order;

import java.time.LocalDateTime;

public interface SendOrdersToSuppliers {
   void suppliersOrdersData(Order order);


}

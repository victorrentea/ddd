package victor.training.ddd.order.service;


import victor.training.ddd.order.model.ProductWithQuantity;
import victor.training.ddd.order.model.SupplierId;

import java.util.List;
import java.util.Map;

public interface SupplierService {
   void sendOrders(Map<SupplierId, List<ProductWithQuantity>> result);
}

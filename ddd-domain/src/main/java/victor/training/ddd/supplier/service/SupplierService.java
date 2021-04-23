package victor.training.ddd.supplier.service;


import victor.training.ddd.supplier.model.ProductWithQuantity;
import victor.training.ddd.order.model.SupplierId;

import java.util.List;
import java.util.Map;

public interface SupplierService {
   void sendOrders(Map<Long, List<ProductWithQuantity>> result);
}

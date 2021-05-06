package victor.training.ddd.supplier.service;


import victor.training.ddd.supplier.model.ProductWithQuantity;

import java.util.List;
import java.util.Map;

public interface SupplierService {
   void sendOrders(Map<String, List<ProductWithQuantity>> result);
}

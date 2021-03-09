package victor.training.ddd.adapter;

import victor.training.ddd.order.model.SupplierId;
import victor.training.ddd.order.service.SupplierService;

public class SupplierServiceRestClient implements SupplierService {

   public SupplierId getSupplierIdForProduct(String productId) {
// TODO rest call
      String dummy = "supplier";
      return new SupplierId(dummy);
   }
}

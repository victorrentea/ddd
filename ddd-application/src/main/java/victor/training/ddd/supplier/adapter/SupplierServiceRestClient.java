package victor.training.ddd.supplier.adapter;

import lombok.extern.slf4j.Slf4j;
import victor.training.ddd.order.adapter.Adapter;
import victor.training.ddd.supplier.model.ProductWithQuantity;
import victor.training.ddd.order.model.SupplierId;
import victor.training.ddd.supplier.service.SupplierService;

import java.util.List;
import java.util.Map;

@Adapter
@Slf4j
public class SupplierServiceRestClient implements SupplierService {

   public String getSupplierIdForProduct(String productId) {
// TODO rest call
      String dummy = "supplier";
      return  dummy;
   }

   @Override
   public void sendOrders(Map<Long, List<ProductWithQuantity>> result) {
      //new RestTemplate().post
      System.out.println(result);
   }
}

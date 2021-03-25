package victor.training.ddd.adapter;

import org.springframework.web.client.RestTemplate;
import victor.training.ddd.order.model.ProductWithQuantity;
import victor.training.ddd.order.model.SupplierId;
import victor.training.ddd.order.service.SupplierService;

import java.util.List;
import java.util.Map;

@Adapter
public class SupplierServiceRestClient implements SupplierService {

   public SupplierId getSupplierIdForProduct(String productId) {
// TODO rest call
      String dummy = "supplier";
      return new SupplierId(dummy);
   }

   @Override
   public void sendOrders(Map<SupplierId, List<ProductWithQuantity>> result) {
//      new RestTemplate().post

   }
}

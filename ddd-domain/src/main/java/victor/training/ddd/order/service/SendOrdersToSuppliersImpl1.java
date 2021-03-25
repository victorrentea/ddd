package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;
import victor.training.ddd.order.model.ProductWithQuantity;
import victor.training.ddd.order.model.SupplierId;
import victor.training.ddd.product.model.Product;
import victor.training.ddd.product.model.ProductPrice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@Profile("client1")
@RequiredArgsConstructor
public class SendOrdersToSuppliersImpl1  implements SendOrdersToSuppliers{
   private final SupplierService supplierService;

   @Override
   public void suppliersOrdersData(Order order) {

      Map<SupplierId, List<ProductWithQuantity>> result = order.orderLines().stream()
          .collect(groupingBy(OrderLine::supplierId,
              mapping(line -> new ProductWithQuantity(getProduct(line), line.itemQuantity()), toList())));
      supplierService.sendOrders(result);
   }
   private Product getProduct(OrderLine line) {
      return new Product(line.productId(),"dummmy",new ProductPrice(BigDecimal.ONE,BigDecimal.ONE,BigDecimal.ONE));
   }
}

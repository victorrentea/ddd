package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;
import victor.training.ddd.order.model.ProductWithCount;
import victor.training.ddd.order.model.SupplierId;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
   private final SupplierService supplierService;


   public Map<SupplierId, List<ProductWithCount>> method(Order order) {
       order.orderLines().stream()
           .collect(groupingBy(OrderLine::productId));
      return null;
   }

}

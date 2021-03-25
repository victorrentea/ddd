package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;
import victor.training.ddd.order.model.ProductWithQuantity;
import victor.training.ddd.order.model.SupplierId;
import victor.training.ddd.product.model.Product;
import victor.training.ddd.product.model.ProductPrice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
   private final SupplierService supplierService;


   public Map<SupplierId, List<ProductWithQuantity>> suppliersOrdersData(Order order) {

      Map<SupplierId, List<ProductWithQuantity>> result = order.orderLines().stream()
          .collect(groupingBy(OrderLine::supplierId,
              mapping(line -> new ProductWithQuantity(getProduct(line), line.itemQuantity()), toList())));
//
//      Map<SupplierId,List<ProductWithQuantity>> result = new HashMap<>();
//       for (OrderLine orderLine:order.orderLines()) {
//            List<ProductWithQuantity> list= result.get(orderLine.supplierId());
//            if(list==null){
//                list = new ArrayList<>();
//                result.put(orderLine.supplierId(),list);
//            }
//            Product dummy = new Product(orderLine.productId(),"dummmy",new ProductPrice(BigDecimal.ONE,BigDecimal.ONE,BigDecimal.ONE));
//            //getProduct from somewhere :) ! // now make dummy one
//            list.add(new ProductWithQuantity(dummy,orderLine.itemQuantity()));
//       }
      supplierService.sendOrders(result);
      return result;
   }

   private Product getProduct(OrderLine line) {
      return new Product(line.productId(),"dummmy",new ProductPrice(BigDecimal.ONE,BigDecimal.ONE,BigDecimal.ONE));
   }

}

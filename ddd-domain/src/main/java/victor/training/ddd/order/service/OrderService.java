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

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
   private final SupplierService supplierService;


   public Map<SupplierId, List<ProductWithQuantity>> suppliersOrdersData(Order order) {
       Map<SupplierId,List<ProductWithQuantity>> result = new HashMap<>();
       for (OrderLine orderLine:order.orderLines()) {
            List<ProductWithQuantity> list= result.get(orderLine.getSupplierId());
            if(list==null){
                list = new ArrayList<>();
                result.put(orderLine.getSupplierId(),list);
            }
            //getProduct from somewhere :) ! // now make dummy one
            Product dummy = new Product(orderLine.productId(),"dummmy",new ProductPrice(BigDecimal.ONE,BigDecimal.ONE,BigDecimal.ONE));
            list.add(new ProductWithQuantity(dummy,orderLine.itemQuantity()));
       }
      return result;
   }

}

package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.OrderLine;
import victor.training.ddd.order.model.ProductWithQuantity;
import victor.training.ddd.order.model.SupplierId;
import victor.training.ddd.order.repo.OrderRepo;
import victor.training.ddd.order.repo.OrderSpec;
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
   private final SendOrdersToSuppliers sendOrdersToSuppliers;
private final OrderRepo orderRepo;

   public void suppliersOrdersData(Order order) {
      sendOrdersToSuppliers.suppliersOrdersData(order);
   }

   public List<Order> search(OrderSearchCriteria criteria) {
      orderRepo.findAll(OrderSpec.clientId(criteria.clientId).and(O`))
   }



}

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

@Profile("client2")
@Service
@RequiredArgsConstructor
public class SendOrdersToSuppliersImpl2 implements SendOrdersToSuppliers{
   private final SupplierService supplierService;

   @Override
   public void suppliersOrdersData(Order order) {
      throw new RuntimeException("e");
   }
}

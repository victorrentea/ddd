package victor.training.ddd.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import victor.training.ddd.common.events.DomainEvents;
import victor.training.ddd.order.model.Product;
import victor.training.ddd.order.model.ProductCategory;
import victor.training.ddd.order.model.ShippingDetails;

import javax.persistence.EntityManager;

//@Component
@RequiredArgsConstructor
public class ProductPlay implements CommandLineRunner {
   private final EntityManager entityManager;

   @Override
   public void run(String... args) throws Exception {
      ShippingDetails shipping = new ShippingDetails(4, "SameDay");
      entityManager.persist(new Product("nume", shipping, 15, ProductCategory.BAB));

      DomainEvents.clearEventsFromTests();
   }
}

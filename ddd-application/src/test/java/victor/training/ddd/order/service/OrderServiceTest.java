package victor.training.ddd.order.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.training.ddd.order.model.*;
import victor.training.ddd.order.repo.CustomerRepo;
import victor.training.ddd.order.repo.OrderRepo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {

   @MockBean
   private OrderRepo orderRepo;
   @MockBean
   private CustomerRepo customerRepo;

   @Autowired
   private OrderService orderService;
   @Test
   void placeOrder() {
      Order order = DummyData.anOrder();
      Customer customer = new Customer();
      when(orderRepo.findById("99")).thenReturn(Optional.of(order));
      when(customerRepo.findById("customerId")).thenReturn(Optional.of(customer));

      orderService.placeOrder("99");

      assertThat(customer.getFidelityPoints()).isEqualTo(2);

//      DomainEventsPublisher.setPublisherFromTests(mock);
   }
}

class DummyData {
   public static Order anOrder() {
      ProductSnapshot product = new ProductSnapshot("1",20);
      Order order = new Order(List.of(new OrderLine(product, 1)));
      order.setCustomerId(new CustomerId("customerId"));
      return order;
   }
}
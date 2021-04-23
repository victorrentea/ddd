package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.repo.OrderRepo;
import victor.training.ddd.order.service.OrderService;
import victor.training.ddd.order.repo.base.EntityRepositoryFactoryBean;

@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
@EnableJpaRepositories(repositoryFactoryBeanClass= EntityRepositoryFactoryBean.class)
public class Application implements CommandLineRunner {
//   public Application(OrderRepo orderRepo, OrderService orderService) {
//      this.orderRepo = orderRepo;
//      this.orderService = orderService;
//   }

   @Bean
   public RestTemplate rest() {
      return new RestTemplate();
   }
   public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
   }

   private final OrderRepo orderRepo;
   private final OrderService  orderService;

   @Override
   public void run(String... args) throws Exception {

      X.oldMethod();

      Order order = new Order("clientId");
      orderRepo.save(order);
      System.out.println(orderRepo.findAll());

      orderService.suppliersOrdersData(order);



   }
}

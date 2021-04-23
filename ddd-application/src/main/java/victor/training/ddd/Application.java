package victor.training.ddd;

import org.springframework.beans.factory.annotation.Autowired;
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
import victor.training.ddd.repo.base.EntityRepositoryFactoryBean;

@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass= EntityRepositoryFactoryBean.class)
public class Application implements CommandLineRunner {
   @Bean
   public RestTemplate rest() {
      return new RestTemplate();
   }
   public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
   }

   @Autowired
   private OrderRepo orderRepo;
   @Autowired
   private OrderService  orderService;

   @Override
   public void run(String... args) throws Exception {

      X.oldMethod();

      Order order = new Order("clientId");
      orderRepo.save(order);
      System.out.println(orderRepo.findAll());

      orderService.suppliersOrdersData(order);

   }
}

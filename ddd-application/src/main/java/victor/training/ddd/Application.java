package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import victor.training.ddd.repo.base.EntityRepositoryFactoryBean;

@EnableBinding(AllChannels.class)
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
@EnableJpaRepositories(repositoryFactoryBeanClass= EntityRepositoryFactoryBean.class)
public class Application {
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

}

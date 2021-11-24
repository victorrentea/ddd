package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;


//interface AllChannels {
//   @Output("ordersConfirmedOut")
//   MessageChannel ordersConfirmedOut();
//
//   @Input("ordersConfirmedIn")
//   SubscribableChannel ordersConfirmedIn();
//}
//
//
//@EnableBinding(AllChannels.class)
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
@EnableJpaRepositories(repositoryFactoryBeanClass= victor.training.ddd.common.repo.base.CustomJpaRepositoryFactoryBean.class)
public class Application {

   public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
   }

   @Bean
   public RestTemplate rest() {
      return new RestTemplate();
   }

}
package victor.training.ddd.agile;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import victor.training.ddd.agile.common.CustomJpaRepositoryImpl.FactoryBean;


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
@EnableJpaRepositories(repositoryFactoryBeanClass= FactoryBean.class)
public class Application {

   public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
   }

   @Bean
   public RestTemplate rest() {
      return new RestTemplate();
   }

}
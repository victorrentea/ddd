package victor.training.ddd.agile;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import victor.training.ddd.agile.application.common.CustomJpaRepositoryImpl.FactoryBean;

import java.util.concurrent.Executor;


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
public class Application implements AsyncConfigurer {

   public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
   }

   @Bean
   public RestTemplate rest() {
      return new RestTemplate();
   }

   @Override
   public Executor getAsyncExecutor() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(10);
      executor.setMaxPoolSize(10);
      executor.setQueueCapacity(500);
      executor.setThreadNamePrefix("async-");
      executor.initialize();
      executor.setWaitForTasksToCompleteOnShutdown(true);
      return executor;
   }
}
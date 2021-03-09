package victor.training.ddd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import victor.training.ddd.repo.base.EntityRepositoryFactoryBean;

@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass= EntityRepositoryFactoryBean.class)
public class Application {
   @Bean
   public RestTemplate rest() {
      return new RestTemplate();
   }
   public static void main(String[] args) {
       SpringApplication.run(Application.class, args);
   }
}

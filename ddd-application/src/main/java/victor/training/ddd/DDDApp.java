package victor.training.ddd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import victor.training.ddd.repo.base.EntityRepositoryFactoryBean;

@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass= EntityRepositoryFactoryBean.class)
public class DDDApp {
   public static void main(String[] args) {
       SpringApplication.run(DDDApp.class, args);
   }
}

package victor.training.ddd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;

@EnableAsync
@SpringBootApplication
public class DDDApp {
   public static void main(String[] args) {
       SpringApplication.run(DDDApp.class, args);
   }
}

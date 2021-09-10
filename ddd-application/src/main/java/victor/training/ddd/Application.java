package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@EnableBinding(AllChannels.class)
@EnableAsync
@SpringBootApplication
@RequiredArgsConstructor
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


@RestController
@RefreshScope
class SomeConfigDependent {
   @Value("${dynamic.prop}")
   private String config;
   @PostConstruct
   public void method() {
      System.out.println("Running with config : "  + config);
   }

   @GetMapping("dynamic-prop")
   public String getConfig() {
      return config;
   }
}
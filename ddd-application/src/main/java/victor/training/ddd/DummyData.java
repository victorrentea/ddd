package victor.training.ddd;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

//@Profile("insertDummyData") // in real project
@Component
@RequiredArgsConstructor
public class DummyData implements CommandLineRunner {

   private final EntityManager em;

   @Override
   public void run(String... args) throws Exception {

   }

}

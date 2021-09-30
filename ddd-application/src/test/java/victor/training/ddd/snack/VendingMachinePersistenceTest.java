package victor.training.ddd.snack;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VendingMachinePersistenceTest {
   @Autowired
   private VendingMachineRepo repo;
   private VendingMachine vendingMachine = new VendingMachine(3);

   @BeforeEach
   public final void before() {
      repo.deleteAll();
      vendingMachine.loadProducts(new SlotId(1), 10, new Cents(100));
   }
   @Test
   public void persistsOk() {
      ObjectId id = repo.save(vendingMachine).getId();

      System.out.println(repo.findById(id));
   }

}

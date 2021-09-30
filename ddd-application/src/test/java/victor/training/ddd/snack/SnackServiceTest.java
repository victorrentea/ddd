package victor.training.ddd.snack;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SnackServiceTest {
   @Autowired
   private SnackService snackService;
   @Autowired
   private SnackPileRepo snackPileRepo;
   @Autowired
   private Product2Repo productRepo;
   private ObjectId productId;

   @BeforeEach
   public final void before() {
      Product product = new Product();
      product.setName("Chio");
      productRepo.save(product);
      productId = product.getId();

      SnackPile pile = new SnackPile();
      pile.setSlotId(1);
      pile.setCount(10);
//      pile.setProduct("Chio");
      pile.setProduct(product);
//      pile.rich("13");
      snackPileRepo.save(pile);
   }
   @AfterEach
   public void method() {
      snackPileRepo.deleteAll();
      productRepo.deleteAll();
   }

   @Test
   void optimisticLocking() {
      assertThat(snackService.checkItem(1)).isTrue();
   }
   @Test
   void checkItem() {
      SnackPile pile1 = snackPileRepo.findAll().get(0);
      SnackPile pile2 = snackPileRepo.findAll().get(0);

      System.out.println(pile1 == pile2);

      pile1.setCount(2);

      pile2.setSlotId(2);

      snackPileRepo.save(pile1);
      snackPileRepo.save(pile2);
   }

   @Test
   void deleteReferenced() {
      snackPileRepo.findAll().forEach(System.out::println);
      productRepo.deleteById(productId);
      snackPileRepo.findAll().forEach(System.out::println);
   }
}
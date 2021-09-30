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
//      pile.setSlotId(1);
      pile.setCount(10);
//      pile.setProduct("Chio");
      pile.setProduct(product);
      snackPileRepo.save(pile);
   }
   @AfterEach
   public void method() {
      snackPileRepo.deleteAll();
      productRepo.deleteAll();
   }

   @Test
   void checkItem() {
      assertThat(snackService.checkItem(1)).isTrue();
   }

   @Test
   void deleteReferenced() {
      snackPileRepo.findAll().forEach(System.out::println);
      productRepo.deleteById(productId);
      snackPileRepo.findAll().forEach(System.out::println);
   }
}
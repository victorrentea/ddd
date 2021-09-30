package victor.training.ddd.snack;

import org.assertj.core.api.Assertions;
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
   @Autowired
   private SnackPileService snackPileService;

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
      snackPileRepo.save(pile);
   }
   @Test
   void checkItem() {
      SnackPile pile1v1 = snackPileRepo.findAll().get(0); // thread 1
      SnackPile pile2v1 = snackPileRepo.findAll().get(0); // thread 2


      // REST call de 1 sec // t1
      // REST call de 1 sec // t2
      System.out.println(pile1v1 == pile2v1);

      pile1v1.setCount(2); // initial era 10

      pile2v1.setSlotId(2); // initial 1

      snackPileRepo.save(pile1v1);
      snackPileRepo.save(pile2v1);

      System.out.println("Final : " + snackPileRepo.findAll().get(0));
   }

   @Test
   public void domainEvents() {
      snackPileService.rich();
      Assertions.assertThat(snackPileRepo.count()).isEqualTo(1);
   }

   @Test
   void optimisticLocking() {
      assertThat(snackService.checkItem(1)).isTrue();
   }
   @Test
   void deleteReferenced() {
      snackPileRepo.findAll().forEach(System.out::println);
      productRepo.deleteById(productId);
      snackPileRepo.findAll().forEach(System.out::println);
   }

   @AfterEach
   public void method() {
      snackPileRepo.deleteAll();
      productRepo.deleteAll();
   }
}
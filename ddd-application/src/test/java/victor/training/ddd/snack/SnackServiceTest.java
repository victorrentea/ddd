package victor.training.ddd.snack;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class SnackServiceTest {
   @Autowired
   private SnackService snackService;
   @Autowired
   private SnackPileRepo snackPileRepo;
   @Autowired
   private SnackRepo productRepo;
   private ObjectId productId;
   @Autowired
   private SnackPileService snackPileService;

   @BeforeEach
   public final void before() {
      snackPileRepo.deleteAll();
      productRepo.deleteAll();

      Snack snack = new Snack();
      snack.setName("Chio");
      productId = productRepo.save(snack).getId();

      SnackPile pile = new SnackPile();
      pile.setSlotId(1);
      pile.setCount(10);
      pile.setSnack(snack);
      pile.getTags().add("aa");
      pile.getTags().add("bb");
      snackPileRepo.save(pile);
   }
   @Test
   void optimisticLocking() {
      SnackPile pile1v1 = snackPileRepo.findAll().get(0); // thread 1
      SnackPile pile2v1 = snackPileRepo.findAll().get(0); // thread 2

      pile1v1.setCount(2); // initial era 10

      pile2v1.setSlotId(2); // initial 1

      snackPileRepo.save(pile1v1);

      assertThrows(OptimisticLockingFailureException.class,
          () -> snackPileRepo.save(pile2v1));
   }

   @Test
   public void domainEvents() {
      snackPileService.rich();
      Assertions.assertThat(snackPileRepo.count()).isEqualTo(2);
   }

   @Test
   void validation() {
      SnackPile pile = new SnackPile().setCount(10);
      assertThrows(ConstraintViolationException.class,
          () -> snackPileRepo.save(pile));
   }
   @Test
   void deleteReferenced() {
      snackPileRepo.findAll().forEach(System.out::println);
      productRepo.deleteById(productId);
      snackPileRepo.findAll().forEach(System.out::println);
   }

}
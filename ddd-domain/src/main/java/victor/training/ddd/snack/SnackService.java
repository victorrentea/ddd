package victor.training.ddd.snack;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Value
class MyEvent {
   String a;
}
@Slf4j
@Service
@RequiredArgsConstructor
public class SnackService {
   private final SnackPileRepo repo;
   public boolean checkItem(int slotId) {
      return repo.findBySlotId(slotId).getCount() > 1;
      
   }
}
@Data
@Document
class SnackPile extends AbstractAggregateRoot<SnackPile> {
   @Id
   private ObjectId id = new ObjectId();
   @NotNull // TODO aspect
   private Integer slotId;
   private int count;

   @Version
   private Integer version;

   @DBRef
//   @NotNull TODO
   private Product product;
//   private String product;

   public void rich(String pa) {
      // mai mereu change-uri pe starea interna
//      registerEvent(new MyEvent("event " +pa));
   }
}

@Retention(RetentionPolicy.RUNTIME)
@Service
@interface Ascuns {

}

@Slf4j
@Ascuns
@RequiredArgsConstructor
class SnackPileService {
   private final SnackPileRepo snackPileRepo;
   private final ApplicationEventPublisher eventPublisher;
   @Transactional
   public void rich() {
      SnackPile pile = snackPileRepo.findAll().get(0);
      pile.rich("13");
      log.info("Acum save");
      snackPileRepo.save(pile);
      eventPublisher.publishEvent(new MyEvent("de mana "));
      log.info("Acum ies");
   }
}
@Slf4j
@Service
@RequiredArgsConstructor
class SomeListener {
   private final SnackPileRepo snackPileRepo;
   @TransactionalEventListener
   public void listen(MyEvent event) {
      log.info("Primit event: " + event);
      snackPileRepo.save(new SnackPile());
      throw new RuntimeException("BUm tranactie2");
   }
}
@Data
@Document
class Product {
   @Id
   private ObjectId id;
   private String name;
}

interface SnackPileRepo extends MongoRepository<SnackPile, ObjectId> {
   SnackPile findBySlotId(int slotId);
}

interface Product2Repo extends MongoRepository<Product, ObjectId> {
}
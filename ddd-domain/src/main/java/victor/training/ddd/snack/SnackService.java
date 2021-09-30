package victor.training.ddd.snack;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


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
   @NotNull
   private Integer slotId;
   private int count;

   private List<String> tags = new ArrayList<>();

   @Version // optimistic locking
   private Integer version;

   @DBRef
   private Snack snack;

   public void rich(String pa) {
      // mai mereu change-uri pe starea interna
      registerEvent(new MyEvent("event " +pa));
   }
}

@Slf4j
@Service
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
   @EventListener
   public void listen(MyEvent event) {
      log.info("Primit event: " + event);
      snackPileRepo.save(new SnackPile().setSlotId(1));
   }
}
@Data
@Document
class Snack {
   @Id
   private ObjectId id;
   private String name;
}

interface SnackPileRepo extends MongoRepository<SnackPile, ObjectId> {
   SnackPile findBySlotId(int slotId);
}

interface SnackRepo extends MongoRepository<Snack, ObjectId> {
}
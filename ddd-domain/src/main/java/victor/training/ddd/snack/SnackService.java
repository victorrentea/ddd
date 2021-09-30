package victor.training.ddd.snack;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;


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
   private ObjectId id;
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
      registerEvent(new MyEvent("event " +pa));
   }
}

@Service
class SomeListener {
   @EventListener
   public void listen(MyEvent event) {
      System.out.println("Primit event: " + event);

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
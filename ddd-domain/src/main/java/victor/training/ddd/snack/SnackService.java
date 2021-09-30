package victor.training.ddd.snack;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

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
class SnackPile {
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
package victor.training.ddd.snack;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendingMachineRepo extends MongoRepository<VendingMachine, ObjectId> {
}

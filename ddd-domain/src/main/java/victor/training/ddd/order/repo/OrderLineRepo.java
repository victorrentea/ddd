package victor.training.ddd.order.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import victor.training.ddd.order.model.OrderLine;

public interface OrderLineRepo extends MongoRepository<OrderLine, String> {
}

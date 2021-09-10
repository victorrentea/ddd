package victor.training.ddd.order.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import victor.training.ddd.order.model.Order;

public interface OrderRepo extends MongoRepository<Order, String> {
   // There must be NO OrderLine Repo
//   Stream<Order> findAllByAndCreateTimeAfter(LocalDateTime time);
}
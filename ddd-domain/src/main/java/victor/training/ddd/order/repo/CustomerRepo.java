package victor.training.ddd.order.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import victor.training.ddd.order.model.Customer;

public interface CustomerRepo extends MongoRepository<Customer, String> {
}

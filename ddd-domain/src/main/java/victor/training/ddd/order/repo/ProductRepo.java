package victor.training.ddd.order.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import victor.training.ddd.order.model.Product;

public interface ProductRepo extends MongoRepository<Product, String> {
}

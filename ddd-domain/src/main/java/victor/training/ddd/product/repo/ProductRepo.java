package victor.training.ddd.product.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import victor.training.ddd.product.model.Product;

public interface ProductRepo extends MongoRepository<Product, String> {

}

package victor.training.ddd.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.product.model.Product;

public interface ProductRepo extends JpaRepository<Product, String> {
}

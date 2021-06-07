package victor.training.ddd.product.repo;

import victor.training.ddd.common.repo.CustomJpaRepository;
import victor.training.ddd.product.model.Product;

public interface ProductRepo extends CustomJpaRepository<Product, String> {

}

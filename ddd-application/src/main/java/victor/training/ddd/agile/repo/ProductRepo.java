package victor.training.ddd.agile.repo;

import victor.training.ddd.agile.common.CustomJpaRepository;
import victor.training.ddd.agile.entity.Product;

public interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);
}

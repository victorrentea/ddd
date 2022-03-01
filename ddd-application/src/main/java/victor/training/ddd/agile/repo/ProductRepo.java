package victor.training.ddd.agile.repo;

import victor.training.ddd.agile.entity.Product;
import victor.training.ddd.agile.common.CustomJpaRepository;

public interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);
}

package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.agile.common.CustomJpaRepository;

public interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);
}

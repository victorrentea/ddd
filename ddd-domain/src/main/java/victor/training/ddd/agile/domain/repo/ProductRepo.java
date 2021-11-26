package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.domain.entity.Product;
import victor.training.ddd.common.repo.CustomJpaRepository;

public interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);

   Product findByCode(String code);
}

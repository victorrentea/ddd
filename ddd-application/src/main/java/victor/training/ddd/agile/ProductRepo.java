package victor.training.ddd.agile;

import victor.training.ddd.common.repo.CustomJpaRepository;

public interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);
}

package victor.training.ddd.agile.domain.repo;

import org.springframework.data.jpa.repository.Query;
import victor.training.ddd.agile.domain.model.Product;
import victor.training.ddd.common.repo.CustomJpaRepository;

public interface ProductRepo extends CustomJpaRepository<Product, Long> {
   boolean existsByCode(String code);

   @Query("SELECT s.productId FROM Sprint s where s.id=?1")
   Product findBySprint(Long sprintId);

}

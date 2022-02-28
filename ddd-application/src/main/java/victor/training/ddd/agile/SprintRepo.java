package victor.training.ddd.agile;

import victor.training.ddd.common.repo.CustomJpaRepository;

import java.util.List;

public interface SprintRepo extends CustomJpaRepository<Sprint, Long> {
   List<Sprint> findAllByProductId(long productId);
}

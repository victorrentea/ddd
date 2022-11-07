package victor.training.ddd.agile.repo;

import victor.training.ddd.agile.entity.Sprint;
import victor.training.ddd.agile.common.CustomJpaRepository;

import java.util.List;

public interface SprintRepo extends CustomJpaRepository<Sprint, Long> {
    List<Sprint> findByProductId(Long id);
}

package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.domain.model.Sprint;
import victor.training.ddd.agile.common.CustomJpaRepository;

import java.util.List;

public interface SprintRepo extends CustomJpaRepository<Sprint, Long> {
    List<Sprint> findByProductId(Long id);
}

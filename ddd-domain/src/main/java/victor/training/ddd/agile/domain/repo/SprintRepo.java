package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.domain.entity.Sprint;
import victor.training.ddd.agile.domain.entity.SprintId;
import victor.training.ddd.common.repo.CustomJpaRepository;

public interface SprintRepo extends CustomJpaRepository<Sprint, SprintId> {
}

package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.common.CustomJpaRepository;
import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.agile.domain.model.SprintItem;

public interface SprintItemRepo extends CustomJpaRepository<SprintItem, Long> {
}

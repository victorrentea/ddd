package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.domain.model.SprintBacklogItem;
import victor.training.ddd.common.repo.CustomJpaRepository;

public interface SprintBacklogItemRepo extends CustomJpaRepository<SprintBacklogItem, Long> {
}

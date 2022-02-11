package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.common.repo.CustomJpaRepository;

public interface SprintBacklogItemRepo extends CustomJpaRepository<SprintBacklogItemRepo, Long> {
}

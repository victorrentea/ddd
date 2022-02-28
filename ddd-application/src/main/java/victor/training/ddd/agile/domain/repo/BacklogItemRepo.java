package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.common.repo.CustomJpaRepository;

public interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
}

package victor.training.ddd.agile.repo;

import victor.training.ddd.agile.common.CustomJpaRepository;
import victor.training.ddd.agile.entity.BacklogItem;

public interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
}
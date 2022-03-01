package victor.training.ddd.agile.repo;

import victor.training.ddd.agile.entity.BacklogItem;
import victor.training.ddd.agile.common.CustomJpaRepository;

public interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
}
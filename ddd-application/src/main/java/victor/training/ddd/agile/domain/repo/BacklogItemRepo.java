package victor.training.ddd.agile.domain.repo;

import victor.training.ddd.agile.domain.model.BacklogItem;
import victor.training.ddd.agile.common.CustomJpaRepository;
import victor.training.ddd.agile.domain.model.Sprint;

public interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
//    @Query("SELECT 1 FROM Backlog_item bi WHERE bi.id = ?1 AND bi.sprint.status=STARTED")
    int countByIdAndSprintIdAndSprintStatus(long itemId, Long sprintId, Sprint.Status started);
}
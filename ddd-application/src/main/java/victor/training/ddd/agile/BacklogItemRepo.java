package victor.training.ddd.agile;

import org.springframework.data.jpa.repository.Query;
import victor.training.ddd.common.repo.CustomJpaRepository;

import java.util.List;

public interface BacklogItemRepo extends CustomJpaRepository<BacklogItem, Long> {
   @Query("SELECT bi " +
          "FROM Sprint s " +
          "JOIN s.items sbi " +
          "JOIN BacklogItem bi  ON sbi.backlogItemId = bi.id " +
          "WHERE s.iteration between ?1 and ?2 " +
          "AND sbi.status = 'DONE'")
   List<BacklogItem> findDoneItemsBetweenIterations(int firstIteration, int lastIteration);
}
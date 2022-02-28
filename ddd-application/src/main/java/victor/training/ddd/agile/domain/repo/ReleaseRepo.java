package victor.training.ddd.agile.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.agile.domain.model.Release;

public interface ReleaseRepo extends JpaRepository<Release, Long> {
}

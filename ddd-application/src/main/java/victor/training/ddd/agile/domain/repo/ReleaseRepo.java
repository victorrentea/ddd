package victor.training.ddd.agile.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.agile.domain.model.Release;

import java.util.List;

public interface ReleaseRepo extends JpaRepository<Release, Long> {
   List<Release> findAllByProductId(Long productId);
}

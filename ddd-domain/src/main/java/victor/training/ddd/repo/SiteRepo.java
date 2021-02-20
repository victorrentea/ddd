package victor.training.ddd.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.model.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}

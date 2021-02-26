package victor.training.ddd.customer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.customer.model.Site;

public interface SiteRepo extends JpaRepository<Site, Long> {
}

package victor.training.ddd.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.model.Email;

public interface EmailRepo extends JpaRepository<Email, Long> {
}

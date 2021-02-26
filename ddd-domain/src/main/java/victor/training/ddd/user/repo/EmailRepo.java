package victor.training.ddd.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.user.model.Email;

public interface EmailRepo extends JpaRepository<Email, Long> {
}

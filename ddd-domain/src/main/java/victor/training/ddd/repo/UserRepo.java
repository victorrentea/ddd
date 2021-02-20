package victor.training.ddd.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
}

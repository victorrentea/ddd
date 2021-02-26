package victor.training.ddd.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.user.model.User;
import victor.training.ddd.user.model.User.UserId;

public interface UserRepo extends JpaRepository<User, UserId> {
}

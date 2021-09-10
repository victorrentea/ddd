package victor.training.ddd.user.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import victor.training.ddd.user.model.User;

public interface UserRepo extends MongoRepository<User,String> {
}

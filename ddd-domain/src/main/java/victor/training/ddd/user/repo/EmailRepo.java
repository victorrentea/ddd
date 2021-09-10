package victor.training.ddd.user.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import victor.training.ddd.user.model.Email;

public interface EmailRepo extends MongoRepository<Email, String> {
}

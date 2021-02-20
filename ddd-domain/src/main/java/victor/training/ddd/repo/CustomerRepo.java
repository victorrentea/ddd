package victor.training.ddd.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.ddd.model.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
}

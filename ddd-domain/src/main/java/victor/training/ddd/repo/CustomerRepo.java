package victor.training.ddd.repo;

import victor.training.ddd.model.Customer;
import victor.training.ddd.model.Customer.CustomerId;
import victor.training.ddd.repo.base.CustomJpaRepository;

public interface CustomerRepo extends CustomJpaRepository<Customer, CustomerId> {
}

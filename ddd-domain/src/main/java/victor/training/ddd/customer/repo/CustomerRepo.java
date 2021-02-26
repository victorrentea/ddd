package victor.training.ddd.customer.repo;

import victor.training.ddd.customer.model.Customer;
import victor.training.ddd.customer.model.Customer.CustomerId;
import victor.training.ddd.common.repo.CustomJpaRepository;

public interface CustomerRepo extends CustomJpaRepository<Customer, CustomerId> {
}




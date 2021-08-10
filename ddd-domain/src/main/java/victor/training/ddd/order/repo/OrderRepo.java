package victor.training.ddd.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import victor.training.ddd.order.model.Order;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
   // There must be NO OrderLine Repo
   Stream<Order> findAllByAndCreateTimeAfter(LocalDateTime time);
}
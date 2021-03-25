package victor.training.ddd.order.repo;

import org.springframework.data.jpa.domain.Specification;
import victor.training.ddd.order.model.Order;
import victor.training.ddd.order.model.Order_;

import javax.persistence.criteria.Expression;
import java.time.LocalDateTime;

public class OrderSpec {

   public static Specification<Order> createdBetween(Expression<LocalDateTime> fromDate, Expression<LocalDateTime> toDate) {
      return (root, query, cb) ->cb.and(
          cb.greaterThanOrEqualTo(fromDate, root.get(Order_.createTime)),
          cb.lessThanOrEqualTo(toDate, root.get(Order_.createTime))
          )
          ;
   }
   public static Specification<Order> clientId(Object clientId) {
      return (root, query, cb) ->cb.equal(root.get(Order_.clientId), clientId);

   }
//   public static Specification<Order> payed() {
//      return (root, query, cb) -> cb.isNotNull(root.get(Order_.paymentTime));
//   }
//   public static Specification<Order> shipped() {
//      return (root, query, cb) -> cb.isTrue(root.get(Order_.shipped));
//   }
}

package victor.training.ddd.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import victor.training.ddd.model.Order;
import victor.training.ddd.model.Order_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class OrderSpec {

   public static Specification<Order> payed() {
      return (root, query, cb) -> cb.isTrue(root.get(Order_.payed));
   }
   public static Specification<Order> shipped() {
      return (root, query, cb) -> cb.isTrue(root.get(Order_.shipped));
   }
}

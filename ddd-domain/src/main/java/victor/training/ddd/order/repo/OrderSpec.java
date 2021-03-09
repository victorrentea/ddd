//package victor.training.ddd.order.repo;
//
//import org.springframework.data.jpa.domain.Specification;
//import victor.training.ddd.order.model.Order;
//import victor.training.ddd.order.model.Order_;
//
//public class OrderSpec {
//
//   public static Specification<Order> payed() {
//      return (root, query, cb) -> cb.isNotNull(root.get(Order_.paymentTime));
//   }
//   public static Specification<Order> shipped() {
//      return (root, query, cb) -> cb.isTrue(root.get(Order_.shipped));
//   }
//}

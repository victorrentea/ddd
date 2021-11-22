//package victor.training.ddd.order.facade.dto;
//
//import victor.training.ddd.order.model.Order;
//
//import java.util.List;
//
//import static java.util.stream.Collectors.toList;
//
//public class OrderDto {
//   public String clientId;
//   public List<OrderLineDto> lines;
//
//   public OrderDto() {}
//   public OrderDto(Order order) {
//      clientId= order.clientId();
//      lines = order.orderLines().stream().map(OrderLineDto::new).collect(toList());
//   }
//}

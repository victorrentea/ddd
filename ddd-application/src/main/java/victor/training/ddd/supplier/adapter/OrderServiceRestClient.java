//package victor.training.ddd.supplier.adapter;
//
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.http.client.support.BasicAuthenticationInterceptor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import victor.training.ddd.order.facade.dto.OrderDto;
//import victor.training.ddd.order.facade.dto.OrderLineDto;
//import victor.training.ddd.supplier.model.OrderLineVO;
//import victor.training.ddd.supplier.model.OrderVO;
//import victor.training.ddd.supplier.service.OrderServiceClient;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Data
//@Service
//@RequiredArgsConstructor
//@ConfigurationProperties(prefix = "order.api")
//public class OrderServiceRestClient implements OrderServiceClient {
//   private final RestTemplate rest;
////   @Value("${order.api.base.url}")
//
//   private String baseUrl;
//   private String username;
//   private String password;
//   @Override
//   public OrderVO getOrder(long orderId) {
//      RestTemplate restTemplate = new RestTemplate();
//
//      restTemplate.getInterceptors().add( new BasicAuthenticationInterceptor(username, password));
//
//      OrderDto orderDto = restTemplate.getForObject(baseUrl + "{id}", OrderDto.class, orderId);
//      // orderDto.toVo(); // if the mapping grows BIG and if you DON'T GENERATE THE DTOs From SWAGGER
//      List<OrderLineVO> lines = orderDto.lines.stream().map(this::toOrderLineVO).collect(Collectors.toList());
//      return new OrderVO(orderDto.clientId, lines);
//   }
//
//   private OrderLineVO toOrderLineVO(OrderLineDto dto) {
//      return new OrderLineVO(dto.itemQuantity, dto.supplierId);
//   }
//}
//

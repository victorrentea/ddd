package victor.training.ddd.order.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

//   @PreAuthorize("hasRole('ADMIN')")
//   @PostMapping
//   public void create(@RequestBody OrderDto dto) {
//      facade.create(dto);
//   }
//
//   @GetMapping("{id}")
//   public OrderDto getById(@PathVariable long id) {
//      return facade.getById(id);
//   }
}




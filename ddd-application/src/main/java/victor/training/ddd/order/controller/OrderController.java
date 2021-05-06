package victor.training.ddd.order.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.order.facade.OrderFacade;
import victor.training.ddd.order.facade.dto.OrderDto;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
   private final OrderFacade facade;

   @PreAuthorize("hasRole('ADMIN')")
   @PostMapping
   public void create(@RequestBody OrderDto dto) {
      facade.create(dto);
   }

   @GetMapping("{id}")
   public OrderDto getById(@PathVariable long id) {
      return facade.getById(id);
   }
}




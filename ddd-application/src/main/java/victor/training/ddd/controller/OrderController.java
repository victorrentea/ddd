package victor.training.ddd.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.facade.OrderFacade;
import victor.training.ddd.facade.dto.OrderDto;

@AllArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {
   private final OrderFacade facade;

   @PostMapping
   public void create(@RequestBody OrderDto dto) {
      facade.create(dto);
   }

   @GetMapping("{id}")
   public OrderDto getById(@PathVariable long id) {
      return facade.getById(id);
   }
}




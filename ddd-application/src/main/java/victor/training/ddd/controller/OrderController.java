package victor.training.ddd.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}




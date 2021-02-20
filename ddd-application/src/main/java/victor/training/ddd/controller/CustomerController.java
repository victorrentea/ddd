package victor.training.ddd.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.facade.CustomerFacade;
import victor.training.ddd.facade.dto.CustomerDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerFacade customerFacade;

    @GetMapping("{id}")
    public CustomerDto getById(@PathVariable long id) {
        return customerFacade.findById(id);
    }

    @PostMapping
    public void create(@RequestBody CustomerDto customerDto) {
        customerFacade.registerCustomer(customerDto);
    }
}

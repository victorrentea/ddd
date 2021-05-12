//package victor.training.ddd.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import victor.training.ddd.facade.CustomerFacade;
//import victor.training.ddd.facade.dto.CustomerDto;
//import victor.training.ddd.facade.dto.CustomerSearchCriteria;
//import victor.training.ddd.facade.dto.CustomerSearchResult;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/customers")
//public class CustomerController {
//    private final CustomerFacade customerFacade;
//
//    @PostMapping("search")
//    public List<CustomerSearchResult> search(@RequestBody CustomerSearchCriteria searchCriteria) {
//        return customerFacade.search(searchCriteria);
//    }
//
//    @GetMapping("{id}")
//    public CustomerDto getById(@PathVariable long id) {
//        return customerFacade.findById(id);
//    }
//
//    @PostMapping
//    public void create(@RequestBody CustomerDto customerDto) {
//        //   new Customer()...<<< customerDto
//        customerFacade.registerCustomer(customerDto);
//    }
//}

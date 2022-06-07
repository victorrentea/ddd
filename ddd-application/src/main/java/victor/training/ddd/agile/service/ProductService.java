package victor.training.ddd.agile.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.entity.Product;
import victor.training.ddd.agile.dto.ProductDto;
import victor.training.ddd.agile.entity.Sprint;
import victor.training.ddd.agile.events.SprintFinishedEvent;
import victor.training.ddd.agile.repo.ProductRepo;
import victor.training.ddd.agile.repo.SprintRepo;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final SprintRepo sprintRepo;
    private final MailingListClient mailingListClient;
    private final EmailService emailService;


    @PostMapping("products")
    public Long createProduct(@RequestBody @Validated ProductDto dto) {
        if (productRepo.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Code already defined");
        }
//      ProductOwner productOwner = new ProductOwner(dto.poName, dto.poEmail, dto.poPhone);
        Product product = new Product(dto.getCode(), dto.getName())
                .setTeamMailingList(dto.getMailingList())
                .setOwnerUserid(dto.getPoUserid());
        ;
        return productRepo.save(product).getId();
    }

    @EventListener
    public void onSprintFinishedEvent2(SprintFinishedEvent event) {
        log.debug("me too!");
    }
    @EventListener
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // dagerous, only consider if plannign to break into microservices in <3mon
//    @Async // void + @Async = fire and forget
    public void onSprintFinishedEvent(SprintFinishedEvent event) {
        Sprint sprint = sprintRepo.findOneById(event.getSprintId());
        Product product = productRepo.findOneById(sprint.getProductId());
        log.debug("Sending CONGRATS email to team of product " + product.getCode() + ": They finished the items earlier. They have time to refactor! (OMG!)");
        if (product.getTeamMailingList().isPresent()) {
            List<String> emails = mailingListClient.retrieveEmails(product.getTeamMailingList().get());
            emailService.sendCongratsEmail(emails);
        }
//        throw new RuntimeException("Blows up everyting, incoiuding the original transacation");
    }

    @GetMapping("products/{id}/notify")
    public void notifyOwner(@PathVariable Long id, @RequestParam String message) {
        Product product = productRepo.findOneById(id);
        String userId = product.getOwnerUserid().orElseThrow();
        callExternalApi(userId);
        System.out.println("With the retrieved email, send to it " + message);
    }

    private String callExternalApi(String userId) {
        System.out.println("Call some API " + userId);

        return "email";
    }


    @GetMapping("products/{id}")
    public ProductDto getProduct(@PathVariable long id) {
        Product product = productRepo.findOneById(id);
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCode(product.getCode());
        dto.setMailingList(product.getTeamMailingList().orElse(""));
        return dto;
    }
}

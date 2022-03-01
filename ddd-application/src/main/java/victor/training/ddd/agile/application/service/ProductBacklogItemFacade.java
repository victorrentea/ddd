package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.ProductBacklogItemDto;
import victor.training.ddd.agile.application.dto.ProductBacklogItemDto.Groups;
import victor.training.ddd.agile.domain.event.FreezeProductBacklogItemEvent;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;
import victor.training.ddd.common.DDD.ApplicationService;

@RestController
@ApplicationService
@RequiredArgsConstructor
public class ProductBacklogItemFacade {
   private final ProductBacklogItemRepo productBacklogItemRepo;

   @PostMapping("backlog")
   @Transactional
   // ProductBacklogItemDto is ABUSED : the version + ID is ALWAYS NULL for this case
//       @RequestBody @Valid CreateProductBacklogItemRequest dto) {
   public Long createBacklogItem(@RequestBody @Validated(Groups.Create.class)
                                        ProductBacklogItemDto dto) {
      ProductBacklogItem productBacklogItem =
          new ProductBacklogItem(dto.productId, dto.title, dto.description)

          ;
      return productBacklogItemRepo.save(productBacklogItem).getId();
   }
   // Validation levels
   // Level1: The controller would check some basic constraints (null, length, regex pattern)

   // Level2: deeper in the code, we have to call some external call (REST, DB)
   //    to make sure your email(eg) is valid

   @PutMapping("backlog")   // PUT /api/backlog/13
   @Transactional
   // ProductBacklogItemDto is ABUSED : the productId is ALWAYS NULL for this case
   // Why is it bad? If you look in the param, some fields will be null. always. which ones ?
   // Also bad: swagger OpenAPI will report "productId" as a field in the body of update.
   // option 1: ignore the issue if <2 fields are missing.
   // option 2: [clearest] different models
//       @RequestBody @Valid UpdateProductBacklogItemRequest dto) {
   public void updateBacklogItem(@RequestBody @Validated(Groups.Update.class)
                                        ProductBacklogItemDto dto) {

      // TODO if Backlog Item is COMPLETED, reject the update
      ProductBacklogItem item = productBacklogItemRepo.findOneById(dto.id);
      item
          .setContents(dto.title, dto.description)
          .setVersion(dto.version);
   }
   @EventListener
   @Transactional
//   public void onItemFreeze(SprintBacklogItemCompletedEvent event) {
   public void onItemFreeze(FreezeProductBacklogItemEvent event) {
//      sprint = ...event.getSprintBacklogItemId()
      ProductBacklogItem item = productBacklogItemRepo.findOneById(event.getProductBacklogItemId());
      item.freeze();
   }
//   private final SprintRepo sprintRepo;

   @GetMapping("backlog/{id}")
   public ProductBacklogItemDto getBacklogItem(@PathVariable long id) {
      ProductBacklogItem productBacklogItem = productBacklogItemRepo.findOneById(id);
      return new ProductBacklogItemDto(productBacklogItem);
   }

   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      productBacklogItemRepo.deleteById(id);
   }
}

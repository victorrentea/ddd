package victor.training.ddd.agile.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import victor.training.ddd.agile.application.dto.ProductBacklogItemDto;
import victor.training.ddd.agile.application.dto.ProductBacklogItemDto.Groups;
import victor.training.ddd.agile.domain.model.ProductBacklogItem;
import victor.training.ddd.agile.domain.repo.ProductBacklogItemRepo;

@RestController
@RequiredArgsConstructor
public class BacklogItemService {
   private final ProductBacklogItemRepo productBacklogItemRepo;

   @PostMapping("backlog")
   @Transactional
   // ProductBacklogItemDto is ABUSED : the version + ID is ALWAYS NULL for this case
   public Long createBacklogItem(
       @RequestBody @Validated(Groups.Create.class) ProductBacklogItemDto dto) {
//       @RequestBody @Valid CreateProductBacklogItemRequest dto) {
      ProductBacklogItem productBacklogItem = new ProductBacklogItem(dto.productId)
          .setContents(dto.title, dto.description)
          ;
      return productBacklogItemRepo.save(productBacklogItem).getId();
   }
   // Validation levels
   // Level1: The controller would check some basic constraints (null, length, regex pattern)

   // Level2: deeper in the code, we have to call some external call (REST, DB)
   //    to make sure your email(eg) is valid

   @PutMapping("backlog")
   @Transactional
   // ProductBacklogItemDto is ABUSED : the productId is ALWAYS NULL for this case
   // Why is it bad? If you look in the param, some fields will be null. always. which ones ?
   // Also bad: swagger OpenAPI will report "productId" as a field in the body of update.
   // option 1: ignore the issue if <2 fields are missing.
   // option 2: [clearest] different models
   public void updateBacklogItem(@RequestBody @Validated(Groups.Update.class) ProductBacklogItemDto dto) {
//       @RequestBody @Valid UpdateProductBacklogItemRequest dto) {

      // TODO if Backlog Item is COMPLETED, reject the update
      ProductBacklogItem item = productBacklogItemRepo.findOneById(dto.id);
      item
          .setContents(dto.title, dto.description)
          .setVersion(dto.version);
   }


   @GetMapping("backlog/{id}")
   public ProductBacklogItemDto getBacklogItem(@PathVariable long id) {
      ProductBacklogItem productBacklogItem = productBacklogItemRepo.findOneById(id);
      ProductBacklogItemDto dto = new ProductBacklogItemDto();
      dto.id = productBacklogItem.getId();

      dto.productId = productBacklogItem.getProductId();
      dto.description = productBacklogItem.getDescription();
      dto.title = productBacklogItem.getTitle();
      dto.version = productBacklogItem.getVersion();
      return dto;
   }

   @DeleteMapping("backlog/{id}")
   public void deleteBacklogItem(@PathVariable long id) {
      productBacklogItemRepo.deleteById(id);
   }
}

package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import victor.training.ddd.agile.BacklogItemController.BacklogItemDto;
import victor.training.ddd.agile.ProductController.ProductDto;
import victor.training.ddd.agile.SprintController.AddBacklogItemRequest;
import victor.training.ddd.agile.SprintController.LogHoursRequest;
import victor.training.ddd.agile.SprintController.SprintDto;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class LargeFlow {
   @Autowired
   ProductController products;
   @Autowired
   BacklogItemController backlogItems;
   @Autowired
   SprintController sprints;
   @Autowired
   ReleaseController releases;

   @Test
   void largeFlow() {
      Long productId = products.createProduct(new ProductDto()
          .setCode("::PNM::")
          .setName("::ProductName::")
          .setMailingList("::MailList::"));

      assertThatThrownBy(() -> products.createProduct(new ProductDto().setCode("::PNM::"))).isInstanceOf(IllegalArgumentException.class);

      assertThat(products.getProduct(productId))
          .extracting(ProductDto::getCode, ProductDto::getName, ProductDto::getMailingList)
          .isEqualTo(List.of("::PNM::", "::ProductName::", "::MailList::"));

      Long sprintId = sprints.createSprint(new SprintDto()
          .setProductId(productId)
          .setPlannedEnd(LocalDate.now().plusDays(14)));

      assertThat(sprints.getSprint(sprintId))
          .matches(s -> s.getIteration() == 1)
          .matches(s -> s.getPlannedEnd().isAfter(LocalDate.now().plusDays(13)));


      Long itemId = backlogItems.createBacklogItem(new BacklogItemDto()
          .setProductId(productId)
          .setTitle("::item::")
          .setDescription("::itemDescr::"));

      sprints.addItem(sprintId, new AddBacklogItemRequest()
          .setFpEstimation(10)
          .setBacklogId(itemId));

      sprints.startSprint(sprintId);
      assertThatThrownBy(() -> sprints.startSprint(sprintId));

      sprints.startItem(sprintId, itemId);

      sprints.logHours(sprintId, new LogHoursRequest()
          .setBacklogId(itemId)
          .setHours(10));

      sprints.completeItem(sprintId, itemId);

      assertThat(sprints.getSprintMetrics(sprintId).toString()).isEqualTo("idn");

      Release release = releases.createRelease(productId, sprintId);

      assertThat(release.getReleaseNotes()).contains("::item::");


   }
}

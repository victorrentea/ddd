package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.web.BacklogItemController.BacklogItemDto;
import victor.training.ddd.agile.web.ProductController.ProductDto;
import victor.training.ddd.agile.web.SprintController.AddSprintBacklogItemRequest;
import victor.training.ddd.agile.web.SprintController.LogHoursRequest;
import victor.training.ddd.agile.web.SprintController.SprintDto;
import victor.training.ddd.agile.web.SprintController.SprintMetrics;
import victor.training.ddd.agile.domain.entity.Release;
import victor.training.ddd.agile.domain.entity.SprintId;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class LargeFlowTest extends SystemTestBase {

   @Test
   void largeFlow() {
      Long productId = products.createProduct(new ProductDto()
          .setCode("PNM")
          .setName("::ProductName::")
          .setMailingList("::MailList::"));

//      assertThatThrownBy(() -> products.createProduct(new ProductDto().setCode("::PNM::"))).isInstanceOf(IllegalArgumentException.class);

      assertThat(products.getProduct(productId))
          .extracting(ProductDto::getCode, ProductDto::getName, ProductDto::getMailingList)
          .isEqualTo(List.of("PNM", "::ProductName::", "::MailList::"));

      SprintId sprintId = sprints.createSprint(new SprintDto()
          .setProductId(productId)
          .setPlannedEnd(LocalDate.now().plusDays(14)));

      assertThat(sprints.getSprint(sprintId))
          .matches(s -> s.getIteration() == 1)
          .matches(s -> s.getPlannedEnd().isAfter(LocalDate.now().plusDays(13)));


      Long itemId = backlogItems.createBacklogItem(new BacklogItemDto()
          .setProductId(productId)
          .setTitle("::item::")
          .setDescription("::itemDescription::"));

      Long sprintItemId = sprints.addItem(sprintId, new AddSprintBacklogItemRequest()
          .setFpEstimation(2)
          .setBacklogId(itemId));

      sprints.startSprint(sprintId);
      assertThatThrownBy(() -> sprints.startSprint(sprintId));

      sprints.startItem(sprintId, sprintItemId);

      sprints.logHours(sprintId, new LogHoursRequest()
          .setBacklogId(sprintItemId)
          .setHours(10));

      sprints.completeItem(sprintId, sprintItemId);

      sprints.finishSprint(sprintId);

      System.out.println("Metrics: " + sprints.getSprintMetrics(sprintId));
      assertThat(sprints.getSprintMetrics(sprintId))
          .extracting(SprintMetrics::getConsumedHours, SprintMetrics::getDoneFP, SprintMetrics::getHoursConsumedForNotDone)
          .containsExactly(10, 2, 0);

      Release release = releases.createRelease(sprintId);

      assertThat(release.getVersion()).isEqualTo("1.0");
      assertThat(release.getSprintIteration()).isEqualTo(1);
      assertThat(release.getReleaseNotes()).contains("::item::");


   }
}

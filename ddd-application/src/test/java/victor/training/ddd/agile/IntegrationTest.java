package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.dto.*;
import victor.training.ddd.agile.entity.Release;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class IntegrationTest extends SystemTestBase {

   @Test
   void longWorkflow() {
      ProductDto productDto = new ProductDto()
          .setCode("PNM")
          .setName("::ProductName::")
          .setMailingList("::MailList::");
      Long productId = products.createProduct(productDto);
      assertThatThrownBy(() -> products.createProduct(productDto)).describedAs("cannot create with same code");

      assertThat(products.getProduct(productId))
          .extracting(ProductDto::getCode, ProductDto::getName, ProductDto::getMailingList)
          .isEqualTo(List.of("PNM", "::ProductName::", "::MailList::"));

      Long sprintId = sprints.createSprint(new CreateSprintRequest()
          .setProductId(productId)
          .setPlannedEnd(LocalDate.now().plusDays(14)));

      assertThat(sprints.getSprint(sprintId))
          .matches(s -> s.getIteration() == 1)
          .matches(s -> s.getPlannedEndDate().isAfter(LocalDate.now().plusDays(13)));


      Long backlogItemId = backlogItems.createBacklogItem(new BacklogItemDto()
          .setProductId(productId).setTitle("::item1::").setDescription("::descr::"));

      BacklogItemDto backlogDto = backlogItems.getBacklogItem(backlogItemId);
      backlogDto.description += "More Text";
      backlogItems.updateBacklogItem(backlogDto);

      assertThatThrownBy(() -> {
             BacklogItemDto backlogDto2 = backlogItems.getBacklogItem(backlogItemId);
             backlogDto2.title = null;
             backlogItems.updateBacklogItem(backlogDto2);
          }).describedAs("title null should be rejected");

      Long itemId = sprints.addItem(sprintId, new AddBacklogItemRequest()
          .setFpEstimation(2)
          .setBacklogId(backlogItemId));

      sprints.startSprint(sprintId);
      assertThatThrownBy(() -> sprints.startSprint(sprintId)).describedAs("cannot start again");
      assertThatThrownBy(() -> sprints.completeItem(sprintId, itemId)).describedAs("must first start item");

      sprints.startItem(sprintId, itemId);
      assertThatThrownBy(() -> sprints.startItem(sprintId, itemId)).describedAs("cannot start again");

      sprints.logHours(sprintId, new LogHoursRequest(itemId, 10));

      sprints.completeItem(sprintId, itemId);

      sprints.endSprint(sprintId);

      System.out.println("Metrics: " + sprints.getSprintMetrics(sprintId));

      assertThat(sprints.getSprintMetrics(sprintId))
          .extracting(SprintMetrics::getConsumedHours, SprintMetrics::getDoneFP, SprintMetrics::getHoursConsumedForNotDone)
          .containsExactly(10, 2, 0);

      Release release = releases.createRelease(productId, sprintId);

      assertThat(release.getReleaseNotes()).contains("::item1::");
      assertThat(release.getVersion()).isEqualTo("1.0");

      // try to update a done backlog item
      BacklogItemDto backlogDto2 = backlogItems.getBacklogItem(backlogItemId);
      backlogDto2.description += "IllegalChange";

      // TODO new feature: uncomment below: should fail
      // assertThatThrownBy(() -> backlogItems.updateBacklogItem(backlogDto2)).describedAs("cannot edit done item");
   }
}

package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.application.dto.*;
import victor.training.ddd.agile.application.dto.SprintMetrics;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WorkflowTest extends SystemTestBase {

   @Test
   void integrationTest() {
      ProductDto productDto = new ProductDto()
          .setCode("PNM")
          .setPoEmail("boss@comp.org")
          .setPoName("Boss")
          .setName("::ProductName::")
          .setMailingList("::MailList::");
      Long productId = productApi.createProduct(productDto);
      assertThatThrownBy(() -> productApi.createProduct(productDto)).describedAs("cannot create with same code");

      assertThat(productApi.getProduct(productId))
          .extracting(ProductDto::getCode, ProductDto::getName, ProductDto::getMailingList)
          .isEqualTo(List.of("PNM", "::ProductName::", "::MailList::"));

      Long sprintId = sprintFacade.createSprint(new CreateSprintRequest()
          .setProductId(productId)
          .setPlannedEnd(LocalDate.now().plusDays(14)));

      assertThat(sprintFacade.getSprint(sprintId))
          .matches(s -> s.getIteration() == 1)
          .matches(s -> s.getPlannedEnd().isAfter(LocalDate.now().plusDays(13)));


      Long productBacklogItemId = backlogItemApi.createBacklogItem(new ProductBacklogItemDto()
          .setProductId(productId)
          .setTitle("::item1::")
          .setDescription("::descr::"));

      ProductBacklogItemDto backlogDto = backlogItemApi.getBacklogItem(productBacklogItemId);
      backlogDto.description += "More Text";
      backlogItemApi.updateBacklogItem(backlogDto);

      String itemId = sprintFacade.addItem(sprintId, new AddBacklogItemRequest()
          .setFpEstimation(2)
          .setProductBacklogId(productBacklogItemId));

      sprintFacade.startSprint(sprintId);
      assertThatThrownBy(() -> sprintFacade.startSprint(sprintId)).describedAs("cannot start again");
      assertThatThrownBy(() -> sprintFacade.completeItem(sprintId, itemId)).describedAs("must first start item");

      sprintFacade.startItem(sprintId, itemId);
      assertThatThrownBy(() -> sprintFacade.startItem(sprintId, itemId)).describedAs("cannot start again");

      sprintFacade.logHours(sprintId, new LogHoursRequest(itemId, 10));

      sprintFacade.completeItem(sprintId, itemId);

      sprintFacade.endSprint(sprintId);

      System.out.println("Metrics: " + sprintFacade.getSprintMetrics(sprintId));

      assertThat(sprintFacade.getSprintMetrics(sprintId))
          .extracting(SprintMetrics::getConsumedHours, SprintMetrics::getDoneFP, SprintMetrics::getHoursConsumedForNotDone)
          .containsExactly(10, 2, 0);

      ReleaseDto release = releases.createRelease(productId, sprintId);

      assertThat(release.getReleaseNotes()).contains("::item1::");
      assertThat(release.getVersion()).isEqualTo("1.0");

      // try to update a done backlog item
      ProductBacklogItemDto backlogDto2 = backlogItemApi.getBacklogItem(productBacklogItemId);
      backlogDto2.description += "IllegalChange";

      // TODO Question: can we change
      assertThatThrownBy(() -> backlogItemApi.updateBacklogItem(backlogDto2)).describedAs("cannot edit done item");
   }
}

//package victor.training.ddd.agile;
//
//import org.junit.jupiter.api.Test;
//import victor.training.ddd.agile.BacklogItemController.BacklogItemDto;
//import victor.training.ddd.agile.ProductController.ProductDto;
//import victor.training.ddd.agile.SprintController.AddBacklogItemRequest;
//import victor.training.ddd.agile.SprintController.LogHoursRequest;
//import victor.training.ddd.agile.SprintController.SprintDto;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//public class LargeFlowTest extends SystemTestBase {
//
//   @Test
//   void longAndHappy() {
//      ProductDto productDto = new ProductDto()
//          .setCode("PNM")
//          .setName("::ProductName::")
//          .setMailingList("::MailList::");
//      Long productId = products.createProduct(productDto);
//      assertThatThrownBy(() -> products.createProduct(productDto)).describedAs("cannot create with same code");
//
//      assertThat(products.getProduct(productId))
//          .extracting(ProductDto::getCode, ProductDto::getName, ProductDto::getMailingList)
//          .isEqualTo(List.of("PNM", "::ProductName::", "::MailList::"));
//
//      Long sprintId = sprints.createSprint(new SprintDto()
//          .setProductId(productId)
//          .setPlannedEnd(LocalDate.now().plusDays(14)));
//
//      assertThat(sprints.getSprint(sprintId))
//          .matches(s -> s.getIteration() == 1)
//          .matches(s -> s.getPlannedEnd().isAfter(LocalDate.now().plusDays(13)));
//
//
//      Long itemId = backlogItems.createBacklogItem(new BacklogItemDto()
//          .setProductId(productId).setTitle("::item1::").setDescription("::descr::"));
//
//      sprints.addItem(sprintId, new AddBacklogItemRequest()
//          .setFpEstimation(2)
//          .setBacklogId(itemId));
//
//      sprints.startSprint(sprintId);
//      assertThatThrownBy(() -> sprints.startSprint(sprintId)).describedAs("cannot start again");
//
//      sprints.startItem(sprintId, itemId);
//      assertThatThrownBy(() -> sprints.startItem(sprintId, itemId)).describedAs("cannot start again");
//
//      sprints.logHours(sprintId, new LogHoursRequest()
//          .setBacklogId(itemId)
//          .setHours(10));
//
//      sprints.completeItem(sprintId, itemId);
//
//      sprints.endSprint(sprintId);
//
//      assertThat(sprints.getSprintMetrics(sprintId))
//          .matches(m -> m.consumedHours == 10)
//          .matches(m -> m.doneFP == 2)
//          .matches(m -> m.hoursConsumedForNotDone == 0);
//
//      Release release = releases.createRelease(productId, sprintId);
//
//      assertThat(release.getReleaseNotes()).contains("::item1::");
//      assertThat(release.getVersion()).isEqualTo("1.0");
//
//
//   }
//}

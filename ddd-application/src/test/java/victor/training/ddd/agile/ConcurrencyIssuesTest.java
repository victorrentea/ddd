package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.application.dto.BacklogItemDto;
import victor.training.ddd.agile.application.dto.ProductDto;

public class ConcurrencyIssuesTest extends AbstractSystemTestBase {

   @Test
   void lazyUser() {
      Long productId = products.createProduct(new ProductDto()
          .setName("::name::")
          .setCode("PNM")
          .setMailingList("a"));

      Long itemId = backlogItems.createBacklogItem(new BacklogItemDto(backlogItem)
          .setProductId(productId)
          .setTitle("I don't know exactly what I want..."));


      /* joe */ BacklogItemDto dataSentToBrowser1 = backlogItems.getBacklogItem(itemId);
      /* joe */ dataSentToBrowser1.setDescription("to make ice cream");
      /* joe */ // leaves for lunch without saving

      /* bob */ BacklogItemDto dataSentToBrowser2 = backlogItems.getBacklogItem(itemId);
      /* bob */ dataSentToBrowser2.setDescription("build space rockets");
      /* bob */ backlogItems.updateBacklogItem(dataSentToBrowser2);

      /* joe */ // returns from lunch
      /* joe */ backlogItems.updateBacklogItem(dataSentToBrowser1);
   }
}

package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.dto.BacklogItemDto;
import victor.training.ddd.agile.dto.ProductDto;

public class ConcurrencyIssuesTest extends SystemTestBase{

   @Test
   void lazyUser() {
      Long productId = products.createProduct(new ProductDto("PNM", "::name::", "a"));

      Long itemId = backlogItems.createBacklogItem(new BacklogItemDto(productId,"Title", "Descr"));


      /* joe */ BacklogItemDto dataSentToBrowser1 = backlogItems.getBacklogItem(itemId);
      /* joe */ dataSentToBrowser1.setDescription("sa faca inghetata");
      /* joe */ // leaves for lunch without saving

      /* bob */ BacklogItemDto dataSentToBrowser2 = backlogItems.getBacklogItem(itemId);
      /* bob */ dataSentToBrowser2.setDescription("rachete!");
      /* bob */ backlogItems.updateBacklogItem(dataSentToBrowser2);

      /* joe */ // returns from lunch
      /* joe */ backlogItems.updateBacklogItem(dataSentToBrowser1);
   }
}

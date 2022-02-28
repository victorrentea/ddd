package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.application.dto.BacklogItemDto;
import victor.training.ddd.agile.application.dto.ProductDto;

public class ConcurrencyIssuesTest extends SystemTestBase{

   @Test
   void lazyUser() {
      Long productId = products.createProduct(new ProductDto()
          .setName("::name::")
          .setCode("PNM")
          .setMailingList("a"));

      Long itemId = backlogItems.createBacklogItem(new BacklogItemDto()
          .setProductId(productId)
          .setTitle("Vreau ceva da nu stiu ce"));


      /* joe */ BacklogItemDto dataSentToBrowser1 = backlogItems.getBacklogItem(itemId);
      /* joe */ dataSentToBrowser1.description ="sa faca inghetata";
      /* joe */ // leaves for lunch without saving

      /* bob */ BacklogItemDto dataSentToBrowser2 = backlogItems.getBacklogItem(itemId);
      /* bob */ dataSentToBrowser2.description="rachete!";
      /* bob */ backlogItems.updateBacklogItem(dataSentToBrowser2);

      /* joe */ // returns from lunch
      /* joe */ backlogItems.updateBacklogItem(dataSentToBrowser1);
   }
}

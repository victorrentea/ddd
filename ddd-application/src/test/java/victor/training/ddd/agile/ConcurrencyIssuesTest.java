package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.application.dto.ProductBacklogItemDto;
import victor.training.ddd.agile.application.dto.ProductDto;

public class ConcurrencyIssuesTest extends SystemTestBase{

   @Test
   void lazyUser() {
      Long productId = productApi.createProduct(new ProductDto()
          .setName("::name::")
          .setCode("PNM")
          .setMailingList("a"));

      Long itemId = backlogItemApi.createBacklogItem(new ProductBacklogItemDto()
          .setProductId(productId)
          .setTitle("Vreau ceva da nu stiu ce"));


      /* joe */ ProductBacklogItemDto dataSentToBrowser1 = backlogItemApi.getBacklogItem(itemId);
      /* joe */ dataSentToBrowser1.description ="sa faca inghetata";
      /* joe */ // leaves for lunch without saving

      /* bob */ ProductBacklogItemDto dataSentToBrowser2 = backlogItemApi.getBacklogItem(itemId);
      /* bob */ dataSentToBrowser2.description="rachete!";
      /* bob */ backlogItemApi.updateBacklogItem(dataSentToBrowser2);

      /* joe */ // returns from lunch
      /* joe */ backlogItemApi.updateBacklogItem(dataSentToBrowser1);
   }
}

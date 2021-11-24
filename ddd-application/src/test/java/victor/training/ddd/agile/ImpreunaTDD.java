package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
import victor.training.ddd.agile.BacklogItemController.BacklogItemDto;
import victor.training.ddd.agile.ProductController.ProductDto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImpreunaTDD extends SystemTestBase {
   @Test
   void codErequired() {
       assertThatThrownBy(() ->products.createProduct(new ProductDto().setName("Ceva").setMailingList("a")));
   }
   @Test
   void nameReq() {
      assertThatThrownBy(() ->products.createProduct(new ProductDto().setCode("Ceva").setMailingList("a")));
   }
   @Test
   void mailReq() {
      assertThatThrownBy(() ->products.createProduct(new ProductDto().setCode("Ceva").setName("a")));
   }
   
   @Test
   void userulLenes() {
      Long productId = products.createProduct(new ProductDto()
          .setName("Ceva")
          .setCode("PNM")
          .setMailingList("a"));

      Long itemId = backlogItems.createBacklogItem(new BacklogItemDto()
          .setProductId(productId)
          .setTitle("Vreau ceva da nu stiu ce"));

      BacklogItemDto dataSentToBrowser1 = backlogItems.getBacklogItem(itemId);
      // italian fiind, pleaca la pizza lasand ecranu deschis
      BacklogItemDto dataSentToBrowser2 = backlogItems.getBacklogItem(itemId);
      dataSentToBrowser2.description="rachete!";
      backlogItems.updateBacklogItem(dataSentToBrowser2);
      // se intoarce de la pizzqa user 1
      dataSentToBrowser1.description ="sa faca inghetata";
      backlogItems.updateBacklogItem(dataSentToBrowser1);
   }

   // TODO sa pice
   @Test
   void doiPeDouaItemuri() {
      Long productId = products.createProduct(new ProductDto()
          .setName("Ceva")
          .setCode("PNM")
          .setMailingList("a"));

      Long itemId1 = backlogItems.createBacklogItem(new BacklogItemDto()
          .setProductId(productId)
          .setTitle("Vreau ceva da nu stiu ce")
          .setDescription("RECE")
      );
      BacklogItemDto dataSentToBrowser1 = backlogItems.getBacklogItem(itemId1);
      dataSentToBrowser1.description ="sa faca inghetata";
      // italian fiind, pleaca la pizza lasand ecranu deschis 2h la masa

         BacklogItemDto dataSentToBrowser2 = backlogItems.getBacklogItem(itemId1);
         dataSentToBrowser2.description="rachete!";
         backlogItems.updateBacklogItem(dataSentToBrowser2);

      // se intoarce de la pizzqa user 1
      backlogItems.updateBacklogItem(dataSentToBrowser1);
   }

}

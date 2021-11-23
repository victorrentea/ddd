package victor.training.ddd.agile;

import org.junit.jupiter.api.Test;
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
}

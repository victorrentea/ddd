package victor.training.ddd;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GlobalConfigParams {
   private static boolean flagX;

   public static boolean getFlagX() {
      return flagX;
   }

   @Value("${flagX}")
   public void setFlagX(boolean flagX) {
      GlobalConfigParams.flagX = flagX;
   }
}
// @Service
class X {
   // @Autowired
   public static void oldMethod() {
      if (GlobalConfigParams.getFlagX()) {
         System.out.println("Hello");
      }
         System.out.println("AFTER");
   }
}
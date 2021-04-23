package victor.training.ddd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
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
@Service
class GlobalConfigParamsInstance {
   @Value("${flagX}")
   private boolean flagX;

   public boolean getFlagX() {
      return flagX;
   }
}
@Component
class SpringGateway {
   private static ApplicationContext spring;

   @Autowired
   public  void setSpring(ApplicationContext spring) {
      SpringGateway.spring = spring;
   }

   public static ApplicationContext getSpring() {
      return spring;
   }

   public static <T> T getBean(Class<T> clazz) {
      return spring.getBean(clazz);
   }
   public static GlobalConfigParamsInstance getConfig() {
      return spring.getBean(GlobalConfigParamsInstance.class);
   }
}
// @Service
class X {
   // @Autowired
   public static void oldMethod() {
      if (GlobalConfigParams.getFlagX()) {
         System.out.println("Hello");
      }
      if (SpringGateway.getConfig().getFlagX()) {
         System.out.println("HelloInstance");
      }
         System.out.println("AFTER");
   }
}
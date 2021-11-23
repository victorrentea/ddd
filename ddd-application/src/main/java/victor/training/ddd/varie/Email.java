package victor.training.ddd.varie;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Email {
   private String value;
   private Email() {}
   public Email(String value) {
      this.value = value;
   }
   public String getValue() {
      return value;
   }
}

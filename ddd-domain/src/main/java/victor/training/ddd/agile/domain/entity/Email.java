package victor.training.ddd.agile.domain.entity;

import javax.persistence.Embeddable;


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

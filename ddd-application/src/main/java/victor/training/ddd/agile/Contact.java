package victor.training.ddd.agile;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;

@AllArgsConstructor
@Getter
@Embeddable
public class Contact {
   private String email;
   private String name;
   private String phone;
}

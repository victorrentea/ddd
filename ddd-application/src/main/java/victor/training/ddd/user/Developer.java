package victor.training.ddd.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Developer {
   private Long id;

   private String firstName; // required
   private String middleName;
   private String lastName; // required

   private String profilePhotoUrl;

   private String city; // required

   // both required or both null
   private String streetName;
   private Integer streetNumber;

   private List<ContactChannel> contactChannels = new ArrayList<>();

}

// TODO play in tests

@Data
class ContactChannel {
   private Long id;
   private String value;
   private Type type;
   private Developer developer;

   enum Type {
      FACEBOOK, TWITTER, PHONE
   }
}

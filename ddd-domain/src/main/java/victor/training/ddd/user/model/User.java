package victor.training.ddd.user.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

   @Id
   private ObjectId id;

//   @Id
//   private String id = UUID.randomUUID().toString();

   private String username;

   protected User() {}
   public User(String username) {
      this.username = username;
   }

   public String getUsername() {
      return username;
   }
}

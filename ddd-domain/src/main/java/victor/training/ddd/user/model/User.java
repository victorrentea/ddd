package victor.training.ddd.user.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class User {

   @Id
   private String id = UUID.randomUUID().toString();

   private String username;

   protected User() {}
   public User(String username) {
      this.username = username;
   }

   public String getUsername() {
      return username;
   }
}

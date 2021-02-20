package victor.training.ddd.model;

import lombok.Data;
import lombok.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class User {

   @Data
   @Embeddable
   public static class UserId implements Serializable {
      private String uuid = UUID.randomUUID().toString();

      protected UserId() {} // for hibernate
      public UserId(String uuid) {
         this.uuid = uuid;
      }
   }
   @EmbeddedId // generated in Java
   private UserId id = new UserId();

   private String username;

   protected User() {}
   public User(String username) {
      this.username = username;
   }

   public String getUsername() {
      return username;
   }
}

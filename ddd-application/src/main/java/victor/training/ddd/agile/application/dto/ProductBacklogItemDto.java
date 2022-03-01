package victor.training.ddd.agile.application.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProductBacklogItemDto {
   public interface Groups {
      public interface Create {}
      public interface Update {}

   }
   @NotNull(groups = {Groups.Update.class})
   public Long id;

   @NotNull(groups = {Groups.Create.class})
   public Long productId;

   @NotNull(groups = {Groups.Create.class,Groups.Update.class})
   public String description;

   @NotNull(groups = {Groups.Create.class,Groups.Update.class})
   @Min(2)
   @Max(30) // usually implemented by FE/client <input type="text" size="30" />. It'll crash anyway!
   public String title;
   // ethical question: when do you want to validate 100% at start
   // 1 if you do non-transactable actions (eg Kafka send)
   // 2 you want to return a nice error to your client, not a opaque error(hiding an SQLException occuring at INSERT time at the end of @Tx)
   // > if that endpoint is called from MY  FRONTEND do NOT repeat validation in backed> my FE is my friend. they should validate on their side. in the ng/react/vue/vanilla
   // unless you want to be nice to your hacker (curl)
   // 3 your client is a dev from another team (microservice call)

   @NotNull(groups = {Groups.Update.class})
   public Long version;
}
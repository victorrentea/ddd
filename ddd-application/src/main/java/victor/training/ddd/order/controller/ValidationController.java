package victor.training.ddd.order.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.ddd.facade.dto.AddressDto;
import victor.training.ddd.order.controller.Validation.DraftGroup;
import victor.training.ddd.order.controller.Validation.SubmittedGroup;

import javax.validation.constraints.NotNull;


@RestController
public class ValidationController {

   @PostMapping("address")
   public String setAddress(@Validated @RequestBody AddressDto address) {
      return "OK; " + address;
   }
   @PostMapping("draft")
   public String saveDraft(@Validated(DraftGroup.class) @RequestBody Contract contract) {
      return "OK; " + contract;
   }
   @PostMapping("submit")
   public String submit(@Validated(SubmittedGroup.class) @RequestBody Contract contract) {
      return "OK; " + contract;
   }

}

   interface Validation {
      interface SubmittedGroup {}
      interface DraftGroup {}
   }

class Contract {
   @NotNull
   public String name;
   @NotNull(groups = {SubmittedGroup.class})
   public String detalii;

   enum Status {
      DRAFT,
      SUBMITTED
   }
   public Status status;

}
package victor.training.ddd;

import victor.training.ddd.facade.dto.AddressDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationPlay {

   public static void main(String[] args) {
      Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

      AddressDto dto = new AddressDto();
      Set<ConstraintViolation<AddressDto>> violations = validator.validate(dto);

      for (ConstraintViolation<AddressDto> violation : violations) {
         System.out.println(violation);
      }

   }
}

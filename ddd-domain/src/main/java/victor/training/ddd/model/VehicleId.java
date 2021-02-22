package victor.training.ddd.model;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

//@Embeddable
public class VehicleId {
   private final String value;

   public VehicleId(String value) {
      if (!value.matches("\\w-\\d+")) {
         throw new IllegalArgumentException("...");
      }
      if (VehicleType.isValidCode(value.substring(0,1))) {
         throw new IllegalArgumentException("...");
      }
      this.value = value;
   }
   public VehicleType getType() {
      return null; // decide based on the first letter of my semantic-rich ID
   }

   public String value() {
      return value;
   }
}
enum VehicleType {
   CAR("C"),
   BIKE("B");
   public final String code;
   VehicleType(String code) {
      this.code = code;
   }
   public static boolean isValidCode(String code) {
      return Arrays.stream(values()).map(VehicleType::code).collect(toSet()).contains(code);
   }

   public String code() {
      return code;
   }
}
package victor.training.ddd.agile.application.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CreateSprintRequest {
   @NotNull
   private Long productId;
//   private Long productId2;
//   private Long productId3;
//   private Long productId5;
//   private Long productId4;
//   private Long productId6;
//   private Long productId7;
//   private Long productId8;
   private LocalDate plannedEnd;
}
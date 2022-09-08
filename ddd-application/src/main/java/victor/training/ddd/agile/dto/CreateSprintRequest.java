package victor.training.ddd.agile.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSprintRequest {
   private Long productId;
   private LocalDate plannedEnd;
}
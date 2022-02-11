package victor.training.ddd.agile.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SprintDto {
   public Long productId;
   public LocalDate plannedEnd;
}

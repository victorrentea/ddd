package victor.training.ddd.agile.dto;

import java.time.LocalDate;

public class CreateSprintRequest {
   public Long productId;
   public LocalDate plannedEnd;

    public CreateSprintRequest() {
    }

    public Long getProductId() {
        return this.productId;
    }

    public LocalDate getPlannedEnd() {
        return this.plannedEnd;
    }

    public CreateSprintRequest setProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public CreateSprintRequest setPlannedEnd(LocalDate plannedEnd) {
        this.plannedEnd = plannedEnd;
        return this;
    }
}
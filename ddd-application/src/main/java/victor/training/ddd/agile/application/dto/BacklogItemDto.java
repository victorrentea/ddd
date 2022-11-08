package victor.training.ddd.agile.application.dto;

import lombok.Data;
import victor.training.ddd.agile.domain.model.BacklogItem;

@Data
public class BacklogItemDto {
    private Long id;
    private Long productId;
    private String title;
    private String description;
    private Long version;

    public BacklogItemDto(BacklogItem backlogItem) {
        setId(backlogItem.getId());
        setProductId(backlogItem.getProduct().getId());
        setDescription(backlogItem.getDescription());
        setTitle(backlogItem.getTitle());
        setVersion(backlogItem.getVersion());
    }
}
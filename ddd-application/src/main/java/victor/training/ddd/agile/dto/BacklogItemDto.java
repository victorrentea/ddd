package victor.training.ddd.agile.dto;

public class BacklogItemDto {
   public Long id;
   public Long productId;
   public String title;
   public String description;
   public Long version;

   public BacklogItemDto() {
   }

   public Long getId() {
      return this.id;
   }

   public Long getProductId() {
      return this.productId;
   }

   public String getTitle() {
      return this.title;
   }

   public String getDescription() {
      return this.description;
   }

   public Long getVersion() {
      return this.version;
   }

   public BacklogItemDto setId(Long id) {
      this.id = id;
      return this;
   }

   public BacklogItemDto setProductId(Long productId) {
      this.productId = productId;
      return this;
   }

   public BacklogItemDto setTitle(String title) {
      this.title = title;
      return this;
   }

   public BacklogItemDto setDescription(String description) {
      this.description = description;
      return this;
   }

   public BacklogItemDto setVersion(Long version) {
      this.version = version;
      return this;
   }

   public boolean equals(final Object o) {
      if (o == this) return true;
      if (!(o instanceof BacklogItemDto)) return false;
      final BacklogItemDto other = (BacklogItemDto) o;
      if (!other.canEqual((Object) this)) return false;
      final Object this$id = this.getId();
      final Object other$id = other.getId();
      if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
      final Object this$productId = this.getProductId();
      final Object other$productId = other.getProductId();
      if (this$productId == null ? other$productId != null : !this$productId.equals(other$productId)) return false;
      final Object this$title = this.getTitle();
      final Object other$title = other.getTitle();
      if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
      final Object this$description = this.getDescription();
      final Object other$description = other.getDescription();
      if (this$description == null ? other$description != null : !this$description.equals(other$description))
         return false;
      final Object this$version = this.getVersion();
      final Object other$version = other.getVersion();
      if (this$version == null ? other$version != null : !this$version.equals(other$version)) return false;
      return true;
   }

   protected boolean canEqual(final Object other) {
      return other instanceof BacklogItemDto;
   }

   public int hashCode() {
      final int PRIME = 59;
      int result = 1;
      final Object $id = this.getId();
      result = result * PRIME + ($id == null ? 43 : $id.hashCode());
      final Object $productId = this.getProductId();
      result = result * PRIME + ($productId == null ? 43 : $productId.hashCode());
      final Object $title = this.getTitle();
      result = result * PRIME + ($title == null ? 43 : $title.hashCode());
      final Object $description = this.getDescription();
      result = result * PRIME + ($description == null ? 43 : $description.hashCode());
      final Object $version = this.getVersion();
      result = result * PRIME + ($version == null ? 43 : $version.hashCode());
      return result;
   }

   public String toString() {
      return "BacklogItemDto(id=" + this.getId() + ", productId=" + this.getProductId() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", version=" + this.getVersion() + ")";
   }
}
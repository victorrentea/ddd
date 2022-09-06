package victor.training.ddd.agile.dto;

public class ProductDto {
   public Long id;
   public String code;
   public String name;
   public String mailingList;
   public String poEmail;
   public String poName;
   public String poPhone;

    public ProductDto() {
    }

    public Long getId() {
        return this.id;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getMailingList() {
        return this.mailingList;
    }

    public String getPoEmail() {
        return this.poEmail;
    }

    public String getPoName() {
        return this.poName;
    }

    public String getPoPhone() {
        return this.poPhone;
    }

    public ProductDto setId(Long id) {
        this.id = id;
        return this;
    }

    public ProductDto setCode(String code) {
        this.code = code;
        return this;
    }

    public ProductDto setName(String name) {
        this.name = name;
        return this;
    }

    public ProductDto setMailingList(String mailingList) {
        this.mailingList = mailingList;
        return this;
    }

    public ProductDto setPoEmail(String poEmail) {
        this.poEmail = poEmail;
        return this;
    }

    public ProductDto setPoName(String poName) {
        this.poName = poName;
        return this;
    }

    public ProductDto setPoPhone(String poPhone) {
        this.poPhone = poPhone;
        return this;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ProductDto)) return false;
        final ProductDto other = (ProductDto) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$code = this.getCode();
        final Object other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$mailingList = this.getMailingList();
        final Object other$mailingList = other.getMailingList();
        if (this$mailingList == null ? other$mailingList != null : !this$mailingList.equals(other$mailingList))
            return false;
        final Object this$poEmail = this.getPoEmail();
        final Object other$poEmail = other.getPoEmail();
        if (this$poEmail == null ? other$poEmail != null : !this$poEmail.equals(other$poEmail)) return false;
        final Object this$poName = this.getPoName();
        final Object other$poName = other.getPoName();
        if (this$poName == null ? other$poName != null : !this$poName.equals(other$poName)) return false;
        final Object this$poPhone = this.getPoPhone();
        final Object other$poPhone = other.getPoPhone();
        if (this$poPhone == null ? other$poPhone != null : !this$poPhone.equals(other$poPhone)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ProductDto;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $code = this.getCode();
        result = result * PRIME + ($code == null ? 43 : $code.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $mailingList = this.getMailingList();
        result = result * PRIME + ($mailingList == null ? 43 : $mailingList.hashCode());
        final Object $poEmail = this.getPoEmail();
        result = result * PRIME + ($poEmail == null ? 43 : $poEmail.hashCode());
        final Object $poName = this.getPoName();
        result = result * PRIME + ($poName == null ? 43 : $poName.hashCode());
        final Object $poPhone = this.getPoPhone();
        result = result * PRIME + ($poPhone == null ? 43 : $poPhone.hashCode());
        return result;
    }

    public String toString() {
        return "ProductDto(id=" + this.getId() + ", code=" + this.getCode() + ", name=" + this.getName() + ", mailingList=" + this.getMailingList() + ", poEmail=" + this.getPoEmail() + ", poName=" + this.getPoName() + ", poPhone=" + this.getPoPhone() + ")";
    }
}
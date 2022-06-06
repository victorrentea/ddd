package victor.training.ddd.agile.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


// no Optional<> here. They don't deserve the "honor!"
public class ProductDto {
   private Long id;
   @NotNull
   private String code;

   private String name;
   private String mailingList;
   private String poEmail;
   private String poName;
   private String poPhone;
   private String poUserid;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getMailingList() {
      return mailingList;
   }

   public void setMailingList(String mailingList) {
      this.mailingList = mailingList;
   }

   public String getPoEmail() {
      return poEmail;
   }

   public void setPoEmail(String poEmail) {
      this.poEmail = poEmail;
   }

   public String getPoName() {
      return poName;
   }

   public void setPoName(String poName) {
      this.poName = poName;
   }

   public String getPoPhone() {
      return poPhone;
   }

   public void setPoPhone(String poPhone) {
      this.poPhone = poPhone;
   }

   public String getPoUserid() {
      return poUserid;
   }

   public void setPoUserid(String poUserid) {
      this.poUserid = poUserid;
   }
}
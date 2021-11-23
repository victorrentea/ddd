package victor.training.ddd.order.model;

public enum ProductCategory {
   ELT("Electronics"),
   HOM("Home"),
   SPO("Sports"),
   BAB("Baby"),
   FAS("Fashion");

   public final String label;

   ProductCategory(String label) {
      this.label = label;
   }

}

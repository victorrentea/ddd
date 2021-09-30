package victor.training.ddd.order.model;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;

class OrderServiceTest {

   @Test
   void cancelOrder() {
      Order order = DummyData.aValidOrder();
//      order.setId(12L);

//      new OrderService().applyCoupon(order, "12");

//      verify(ca s-a trimis HTTP/mesaje)

   }


   @Test
   public void test() {

      Order order;

//      order.addOrderLine(new OrderLine(scaun, 1));

//      order.getOrderLines().get(0).setDiscountRate(.8);
//      order.getOrderLines().get(0).setCount

   }
}

class DummyData {
   public static Order aValidOrder() {

      // ce faci daca vrei sa raportezi ambele erori: streetName + city = null
      // Scenariu 1: Client=UI> UI-ul sa faca validarile direct in ecran, pana face POST.
      // Daca primesti date invalide singura varianta este ca un HACKER sa fi evitat UI-ul cu curl/postman
      // NU ITI PASA DE EL : crapi la prima abatere cu Exceptie

      // Scenariu 2: bulk import (upload/import de fisier)
      // vei vrea sa raportezi: randu 2 si 5 au avut erorile :A,B,C,D

      // Scenariu 3: REST API
      // Vrei sa raportezi TOATE erorile la clienti, ca sa fie fericiti
      // a) MUNCITORESC: Iti faci un validator care aduna intr-o List erorile
      // b) @NotNull/@Email/@Length pe DTO-uri javax.validation
            // >> validarile facute pe DTO-uri trebuie tinute in sync cu validarile de pe MODEL
      new Address(null, null, null, null);


      return new Order(asList(new OrderLine(null, 2)));
   }

}
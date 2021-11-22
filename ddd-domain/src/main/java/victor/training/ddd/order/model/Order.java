package victor.training.ddd.order.model;


import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//@Data // NU!
// getteri,

// setteri,

// hashCode-equals >> calc hashCode pe toate campurile,inclusiv pe ID. si cand hibernate atribuie ID, hashcode equals i se modifica;
// Hashcode equals tre implementate doar pe campuri imutabile (fara setteri)

// tostring >> lazy loading invizibil daca lista de lines nu a fost inca incarcata,
///@Getter de obicei ramane
@Entity
@Table(name = "ORDERS") // SQL keyword collision
public class Order {
   @Id
   @GeneratedValue
   private Long id;

   @CreatedDate
   private LocalDateTime createTime;

   private String clientId; // ID of an externally-managed person ?..

   @OneToMany(mappedBy = "order")
   private List<OrderLine> lines = new ArrayList<>();

   // respo: sa tina cele doua capete ale relatiei bidirect in sync,
   public void addLine(OrderLine orderLine) {
      lines.add(orderLine);
      orderLine.setOrder(this);
   }
   // respo: sa tina cele doua capete ale relatiei bidirect in sync,
   public void removeLine(OrderLine orderLine) {
      lines.remove(orderLine);
      orderLine.setOrder(null);
   }

   public List<OrderLine> getLines() {
      return Collections.unmodifiableList(lines);
   }


   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public LocalDateTime getCreateTime() {
      return createTime;
   }

   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }

   public String getClientId() {
      return clientId;
   }

   public Order setClientId(String clientId) {
      this.clientId = clientId;
      return this;
   }

}


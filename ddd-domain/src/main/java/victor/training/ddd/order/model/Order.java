package victor.training.ddd.order.model;


import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data // TODO Explain why @Data + DDD = ❌

@Entity
@Table(name = "ORDERS") // SQL keyword collision
public class Order {
   @Id
   @GeneratedValue
   private Long id;

   @ManyToOne
   private Customer customer;

   @OneToMany(mappedBy = "order") // bidirectional is usually naive
   private List<OrderLine> lines = new ArrayList<>();

   @CreatedDate // Spring Data Magic
   private LocalDateTime createTime;
}


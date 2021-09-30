package victor.training.ddd.order.model;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

@Getter
@Document
public class Cart {
   @Id
   private ObjectId id;
   private final List<CartItem> items = new ArrayList<>();

   @Getter
   public static class CartItem {
      private final ObjectId productId;
      private final int count;

      public CartItem(ObjectId productId, int count) {
         this.productId = requireNonNull(productId);
         if (count <= 0) {
            throw new IllegalArgumentException("Invalid count. Must be positive");
         }
         this.count = count;
      }

      public CartItem addItems(int addedCount) {
         return new CartItem(productId, count + addedCount);
      }
   }

   public List<CartItem> getItems() {
      return unmodifiableList(items);
   }

   public int getProductItemCount() {
      return items.stream().mapToInt(CartItem::getCount).sum();
   }

   public void addProduct(ObjectId productId, int addedItemsCount) {
      if (addedItemsCount <= 0) {
         throw new IllegalArgumentException();
      }
      Optional<CartItem> orderLineOpt = items.stream()
          .filter(line -> line.getProductId().equals(productId))
          .findFirst();

      if (orderLineOpt.isPresent()) {
         CartItem existingLine = orderLineOpt.get();
         this.items.remove(existingLine);
         this.items.add(existingLine.addItems(addedItemsCount));
      } else {
         this.items.add(new CartItem(productId, addedItemsCount));
      }
   }

   public void setProductItemsCount(ObjectId productId, int newItemsCount) {
      removeProduct(productId);
      addProduct(productId, newItemsCount);
   }
   public void removeProduct(ObjectId productId) {
      items.removeIf(item -> item.getProductId().equals(productId));
   }
}


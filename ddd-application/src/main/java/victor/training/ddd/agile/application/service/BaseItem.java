//package victor.training.ddd.agile.application.service;
//
//import javax.persistence.*;
//
//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//abstract class AbstractBacklogItem {
//   @Id
//    @GeneratedValue
//   Long id;
//   String title,description;
//}
//@DiscriminatorValue("PRODUCT_ITEM")
//class ProductBacklogItem extends AbstractBacklogItem {
//
//}
//@DiscriminatorValue("SPRINT_ITEM")
//class SprintBacklogItem extends AbstractBacklogItem {
//   Long sprintId;
//   Long fpEstimate;
//}
//
//
//class T {
//   public void addItemToSprint() {
//      // "UPDATE DISCR='SPRINT_ITEM' WHERE id = ?"
//   }
//}
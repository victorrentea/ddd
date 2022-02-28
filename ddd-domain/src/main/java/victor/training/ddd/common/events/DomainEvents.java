package victor.training.ddd.common.events;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DomainEvents {

   private static ApplicationEventPublisher springPublisher = new NoopPublisher();

   @Autowired // method injected when Spring initializes this bean
   public void injectSpringPublisher(ApplicationEventPublisher publisher) {
      springPublisher = publisher;
   }

   @VisibleForTesting
   public static void clearEventsFromTests() {
      // TODO make sure you call this from a @Rule/JUnit5 Extension before every test
      DomainEvents.springPublisher = new PublisherFakeForTests();
   }

   public static List<Object> getEventsFromTests() {
      return ((PublisherFakeForTests) springPublisher).domainEvents;
   }

   public static void publishEvent(Object domainEvent) {
      springPublisher.publishEvent(domainEvent);
      System.out.println("AFTER ALL LISTENERS ARE NOTIFIED");
   }

   private static class NoopPublisher implements ApplicationEventPublisher {
      @Override
      public void publishEvent(ApplicationEvent event) {
         // internal spring events, never sent by my application
      }

      @Override
      public void publishEvent(Object event) {
         log.warn("Spring event publisher not yet set. Event NOT published: {}", event);
         // TODO consider throwing exception
      }
   }
   private static class PublisherFakeForTests implements ApplicationEventPublisher {
      private final List<Object> domainEvents = new ArrayList<>();
      @Override
      public void publishEvent(ApplicationEvent event) {
         // not interesting
      }

      @Override
      public void publishEvent(Object event) {
         domainEvents.add(event);
      }
   }


}

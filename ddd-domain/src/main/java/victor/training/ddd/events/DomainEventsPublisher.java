package victor.training.ddd.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class DomainEventsPublisher {
   private static ApplicationEventPublisher publisher = new NoopPublisher();

   private static class NoopPublisher implements ApplicationEventPublisher {
      @Override
      public void publishEvent(ApplicationEvent event) {
         log.warn("Spring event publisher not set");
      }

      @Override
      public void publishEvent(Object event) {
         log.warn("Spring event publisher not set");
      }
   }

   static void setPublisher(ApplicationEventPublisher publisher) {
      DomainEventsPublisher.publisher = publisher;
   }

   public static void publish(DomainEvent domainEvent) {
      publisher.publishEvent(domainEvent);
   }

   @Autowired
   public void injectSpringPublisher(ApplicationEventPublisher publisher) {
      setPublisher(publisher);
   }
}

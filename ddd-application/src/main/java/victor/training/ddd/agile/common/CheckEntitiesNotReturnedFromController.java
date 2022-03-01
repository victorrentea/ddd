package victor.training.ddd.agile.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CheckEntitiesNotReturnedFromController implements BeanPostProcessor {
   public static final List<Class<? extends Annotation>> WEB_ANNOTATIONS = List.of(RequestMapping.class, GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class, PatchMapping.class);
   public static final List<Class<? extends Annotation>> JPA_ANNOTATIONS = List.of(Entity.class, Embeddable.class);

   @Override
   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
      if (!bean.getClass().isAnnotationPresent(RestController.class)) {
         return bean;
      }
      for (Method method : bean.getClass().getMethods()) {
         if (WEB_ANNOTATIONS.stream().anyMatch(method::isAnnotationPresent)) {
            if (Stream.of(method.getParameterTypes()).anyMatch(this::isJpaObject)) {
               throw new IllegalArgumentException("Method " + method + " takes JPA annotated object as parameter");
            }
            if (isJpaObject(method.getReturnType())) {
               throw new IllegalArgumentException("Method " + method + " returns JPA annotated object");
            }
         }
      }
      return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
   }

   private boolean isJpaObject(Class<?> parameterType) {
      return JPA_ANNOTATIONS.stream().anyMatch(parameterType::isAnnotationPresent);
   }
}

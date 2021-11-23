package victor.training.ddd.common.repo.base;

import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import victor.training.ddd.common.repo.CustomJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@NoRepositoryBean
public class CustomJpaRepositoryImpl<E, ID> extends SimpleJpaRepository<E, ID> implements CustomJpaRepository<E, ID> {

   private final RepositoryInformation repositoryInformation;
   protected EntityManager entityManager;

   public CustomJpaRepositoryImpl(RepositoryInformation repositoryInformation, EntityManager entityManager) {
      super((Class<E>) repositoryInformation.getDomainType(), entityManager);
      this.repositoryInformation = repositoryInformation;
      this.entityManager = entityManager;


   }

   private Class<?> extractIdClass(RepositoryInformation repositoryInformation) {
      return (Class<?>) Stream.of(repositoryInformation.getRepositoryInterface().getGenericInterfaces())
          .filter(c -> c instanceof ParameterizedType)
          .map(c -> (ParameterizedType) c)
          .filter(c -> c.getRawType().equals(CustomJpaRepository.class))
          .map(c -> c.getActualTypeArguments()[1])
          .findFirst().orElseThrow(() -> new IllegalArgumentException("Could not extract ID class from repository signature : " + repositoryInformation.getRepositoryInterface()));
   }

   @Override
   public E findOneById(ID id) {
      return findById(id).orElseThrow(() -> new EntityNotFoundException("No " + getDomainClass().getSimpleName() + " with id " + id));
   }

   @SneakyThrows
   @Override
   public ID newId() {
      Query nativeQuery = entityManager.createNativeQuery("SELECT HIBERNATE_SEQUENCE.NEXTVAL FROM DUAL");
      long number = ((Number) nativeQuery.getSingleResult()).longValue();
      Class<ID> idClass = (Class<ID>) extractIdClass(repositoryInformation);
      return idClass.getConstructor(Long.class).newInstance(number);
   }

}

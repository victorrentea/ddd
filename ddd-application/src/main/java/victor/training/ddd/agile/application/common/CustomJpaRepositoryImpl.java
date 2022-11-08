package victor.training.ddd.agile.application.common;

import lombok.SneakyThrows;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import victor.training.ddd.agile.common.CustomJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.io.Serializable;
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
      if (idClass.equals(Long.class)) {
         return (ID) Long.valueOf(number);
      } else {
         return idClass.getConstructor(Long.class).newInstance(number);
      }
   }

   // ======== Spring Framework Stuff bellow ==========
   public static class FactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
       extends JpaRepositoryFactoryBean<R, T, ID> {

      public FactoryBean(Class<? extends R> repositoryInterface) {
         super(repositoryInterface);
      }

      @SuppressWarnings("rawtypes")
      protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
         return new RepoFactory(em);
      }
   }

   private static class RepoFactory<E, ID extends Serializable> extends JpaRepositoryFactory {
      public RepoFactory(EntityManager entityManager) {
         super(entityManager);
      }

      @Override
      protected JpaRepositoryImplementation<E, ID> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
         return new CustomJpaRepositoryImpl<E, ID>(information, entityManager);
      }

      @Override
      protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
         return CustomJpaRepositoryImpl.class;
      }
   }
}

package victor.training.ddd.repo.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class EntityRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, ID> {
	
    public EntityRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@SuppressWarnings("rawtypes")
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager em) {
        return new EntityRepositoryFactory(em);
    }
    
    private static class EntityRepositoryFactory<E, ID extends Serializable> extends JpaRepositoryFactory {
        private final EntityManager entityManager;

        public EntityRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
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

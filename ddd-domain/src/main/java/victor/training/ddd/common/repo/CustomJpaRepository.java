package victor.training.ddd.common.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<E, ID> extends JpaRepository<E, ID> {
   /**
    * @param id the PK to lookup
    * @return the Entity. Never null.
    * @throws javax.persistence.EntityNotFoundException when ID not found in DB
    */
   E findOneById(ID id);

   ID newId();
}

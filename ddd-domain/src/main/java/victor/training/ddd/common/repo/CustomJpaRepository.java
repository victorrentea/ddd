package victor.training.ddd.common.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomJpaRepository<E, ID> extends JpaRepository<E, ID> {
   E findExactlyOne(ID id);
   ID newId();
}

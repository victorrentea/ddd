package victor.training.ddd.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import victor.training.ddd.facade.dto.CustomerSearchCriteria;
import victor.training.ddd.facade.dto.CustomerSearchResult;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerSearchRepo {
   private final EntityManager em;

   public List<CustomerSearchResult> search(CustomerSearchCriteria criteria) {
      String jpql = "SELECT new victor.training.ddd.facade.dto.CustomerSearchResult(c.id, c.name)" +
                    " FROM Customer c " +
                    " WHERE 1=1 ";

      Map<String, Object> paramMap = new HashMap<>();

      if (StringUtils.isNotEmpty(criteria.name)) {
         jpql += "  AND UPPER(c.name) LIKE UPPER('%' || :name || '%')   ";
         paramMap.put("name", criteria.name);
      }

      TypedQuery<CustomerSearchResult> query = em.createQuery(jpql, CustomerSearchResult.class);
      for (String paramName : paramMap.keySet()) {
         query.setParameter(paramName, paramMap.get(paramName));
      }
      return query.getResultList();
   }
}

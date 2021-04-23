package victor.training.ddd.order.repo;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import victor.training.ddd.order.facade.dto.CustomerSearchResult;
//import victor.training.ddd.order.facade.dto.CustomerSearchCriteria;


@Repository
@RequiredArgsConstructor
public class CustomerSearchRepo {
   private final EntityManager em;

//   public List<CustomerSearchResult> search(CustomerSearchCriteria criteria) {
//      String jpql = "SELECT new victor.training.ddd.facade.dto.CustomerSearchResult(c.id, c.name)" +
//                    " FROM Customer c " +
//                    " WHERE 1=1 ";
//
//      // TODO Brain: is it ok to JOIN Site too ?
//
//      Map<String, Object> paramMap = new HashMap<>();
//
//      if (StringUtils.isNotEmpty(criteria.name)) {
//         jpql += "  AND UPPER(c.name) LIKE UPPER('%' || :name || '%')   ";
//         paramMap.put("name", criteria.name);
//      }
////      if (StringUtils.isNotEmpty(criteria.name)) {
////         jpql += "  AND EXISTS (SELECT ......) ";
////         paramMap.put("name", criteria.name);
////      }
//
//      TypedQuery<CustomerSearchResult> query = em.createQuery(jpql, CustomerSearchResult.class);
//      for (String paramName : paramMap.keySet()) {
//         query.setParameter(paramName, paramMap.get(paramName));
//      }
//      return query.getResultList();
//   }
}

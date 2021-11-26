package victor.training.ddd.agile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.training.ddd.agile.infra.MailingListService;
import victor.training.ddd.agile.web.BacklogItemController;
import victor.training.ddd.agile.web.ProductController;
import victor.training.ddd.agile.web.ReleaseController;
import victor.training.ddd.agile.web.SprintController;

@SpringBootTest
public abstract class SystemTestBase {
   @Autowired
   ProductController products;
   @Autowired
   BacklogItemController backlogItems;
   @Autowired
   SprintController sprints;
   @Autowired
   ReleaseController releases;
   @MockBean
   MailingListService mailingListService;
}

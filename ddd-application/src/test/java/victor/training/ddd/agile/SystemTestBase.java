package victor.training.ddd.agile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

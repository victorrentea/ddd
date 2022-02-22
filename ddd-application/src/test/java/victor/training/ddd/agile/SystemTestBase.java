package victor.training.ddd.agile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class SystemTestBase {
   @Autowired
   ProductService products;
   @Autowired
   BacklogItemService backlogItems;
   @Autowired
   SprintService sprints;
   @Autowired
   ReleaseService releases;
   @MockBean
   MailingListClient mailingListClient;
}

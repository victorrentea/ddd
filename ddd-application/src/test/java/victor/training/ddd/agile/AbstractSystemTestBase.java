package victor.training.ddd.agile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.training.ddd.agile.service.*;

@SpringBootTest
public abstract class AbstractSystemTestBase {
   @Autowired
   ProductService products;
   @Autowired
   BacklogItemService backlogItems;
   @Autowired
   SprintService sprints;
   @Autowired
   ReleaseService releases;
   @MockBean
   MailingListClient mailingListClientMock;
   @MockBean
   EmailSender emailSenderMock;
}

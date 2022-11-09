package victor.training.ddd.agile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.training.ddd.agile.application.service.*;
import victor.training.ddd.agile.infra.EmailSender;
import victor.training.ddd.agile.infra.MailingListClient;

@SpringBootTest
public abstract class AbstractSystemTestBase {
   @Autowired
   ProductApplicationService products;
   @Autowired
   SprintMetricsApplicationService metricsController;
   @Autowired
   BacklogItemApplicationService backlogItems;
   @Autowired
   SprintApplicationServiceApi sprints;
   @Autowired
   ReleaseApplicationService releases;
   @MockBean
   MailingListClient mailingListClientMock;
   @MockBean
   EmailSender emailSenderMock;
}

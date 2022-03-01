package victor.training.ddd.agile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.training.ddd.agile.application.service.*;

@SpringBootTest
public abstract class SystemTestBase {
   @Autowired
   ProductFacade productApi;
   @Autowired
   ProductBacklogItemFacade backlogItemApi;
   @Autowired
   SprintFacade sprintFacade;
   @Autowired
   ReleaseFacade releases;
   @MockBean
   MailingListClient mailingListClient;
}

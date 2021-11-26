package victor.training.ddd.agile.domain.service;

import victor.training.ddd.agile.domain.entity.Email;

import java.util.List;

public interface IEmailService {
   void sendCongratsEmail(List<Email> emails);

   void sendNotDoneItemsDebrief(String ownerEmail, List<String> notDoneItems);
}

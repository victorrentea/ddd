package victor.training.ddd.agile.domain.service;

import victor.training.ddd.agile.domain.entity.Email;

import java.util.List;

public interface IMailingListService {
   List<Email> retrieveEmails(String mailingList);
}

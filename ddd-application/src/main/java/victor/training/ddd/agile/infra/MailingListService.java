package victor.training.ddd.agile.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import victor.training.ddd.agile.domain.entity.Email;
import victor.training.ddd.agile.domain.service.IMailingListService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MailingListService implements IMailingListService {
   private final RestTemplate rest;

   @Override
   public List<Email> retrieveEmails(String mailingList) {
      String[] emails = rest.getForObject("http://localhost:8989/mailing-list/" + mailingList, String[].class);
      return Stream.of(emails).map(Email::new).collect(Collectors.toList());
   }
}

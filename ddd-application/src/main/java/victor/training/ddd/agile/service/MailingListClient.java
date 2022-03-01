package victor.training.ddd.agile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailingListClient {
   private final RestTemplate rest;

   public List<String> retrieveEmails(String mailingList) {
      String[] emails = rest.getForObject("http://localhost:8989/mailing-list/" + mailingList, String[].class);
      return List.of(emails);
   }
}

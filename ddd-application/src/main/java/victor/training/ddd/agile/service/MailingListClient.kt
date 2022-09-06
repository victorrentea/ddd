package victor.training.ddd.agile.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class MailingListClient(val rest: RestTemplate) {
    fun retrieveEmails(mailingList: String): List<String> {
        val emails = rest.getForObject("http://localhost:8989/mailing-list/$mailingList", Array<String>::class.java)
        return java.util.List.of(*emails)
    }
}
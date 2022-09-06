package victor.training.ddd.agile.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSender {
	
	public void sendEmail(String from, String to, String subject, String message) {
		// implementation goes here. connect to SMTP...
		log.debug("Pretend send email with title " + subject);
	}
}

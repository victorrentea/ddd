package victor.training.ddd.common.adapter;

import org.springframework.stereotype.Service;
import victor.training.ddd.user.service.EmailSender;

// We pretend this talks to an external API
@Service
public class EmailSenderViaSmtp implements EmailSender {
	
	@Override
	public void sendEmail(String from, String to, String subject, String message) {
		// implementation goes here
	}
}

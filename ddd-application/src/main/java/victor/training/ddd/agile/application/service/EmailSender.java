package victor.training.ddd.agile.application.service;

import org.springframework.stereotype.Service;

// Pretend this talks to an external API
@Service
public class EmailSender {
	
	public void sendEmail(String from, String to, String subject, String message) {
		// implementation goes here. connect to SMTP...
		System.out.println("Prented send email " + subject);
	}
}

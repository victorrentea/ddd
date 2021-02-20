package victor.training.ddd.service;

public interface EmailSender {
   void sendEmail(String from, String to, String subject, String message);
}

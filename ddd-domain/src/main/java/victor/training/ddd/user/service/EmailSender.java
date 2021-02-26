package victor.training.ddd.user.service;

public interface EmailSender {
   void sendEmail(String from, String to, String subject, String message);
}

package victor.training.ddd.agile.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmailSender {
    fun sendEmail(from: String?, to: String?, subject: String, message: String?) {
        // implementation goes here. connect to SMTP...
        log.debug("Pretend send email with title $subject")
    }

    companion object {
        private val log = LoggerFactory.getLogger(EmailSender::class.java)
    }
}
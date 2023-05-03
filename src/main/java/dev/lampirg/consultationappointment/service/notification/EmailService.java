package dev.lampirg.consultationappointment.service.notification;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Profile("email")
public class EmailService {

    private MailSender mailSender;

    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    // TODO: this method
    public void sendEmail(SimpleMailMessage message) {
        mailSender.send(message);
    }
}

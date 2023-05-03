package dev.lampirg.consultationappointment.service.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailMessage;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("email")
public class EmailService {

    private MailSender mailSender;

    @Value("${spring.mail.host}")
    private String host;

    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    // TODO: this method
    public void sendEmail(MailMessage message) {
        System.out.println(message);
    }
}

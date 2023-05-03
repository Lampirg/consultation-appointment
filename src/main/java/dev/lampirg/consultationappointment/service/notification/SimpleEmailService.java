package dev.lampirg.consultationappointment.service.notification;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@Profile("email & prod")
public class SimpleEmailService implements EmailService<SimpleMailMessage> {

    private MailSender mailSender;

    public SimpleEmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(SimpleMailMessage message) {
        mailSender.send(message);
    }
}

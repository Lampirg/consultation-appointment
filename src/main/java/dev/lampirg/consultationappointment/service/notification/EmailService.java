package dev.lampirg.consultationappointment.service.notification;

import org.springframework.mail.MailMessage;

public interface EmailService<T extends MailMessage> {
    void sendEmail(T message);
}

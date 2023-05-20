package dev.lampirg.consultationappointment.service.notification;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Profile("email & dev")
public class MockEmailService implements EmailService<SimpleMailMessage> {

    private static Logger logger = Logger.getLogger(MockEmailService.class.getName());
    @Override
    public void sendEmail(SimpleMailMessage message) {
        if (logger.isLoggable(Level.INFO))
            logger.info(String.valueOf(message));
    }
}

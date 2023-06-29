package dev.lampirg.consultationappointment.service.notification

import org.springframework.context.annotation.Profile
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Component
import java.util.logging.Level
import java.util.logging.Logger

@Component
@Profile("email & dev")
class MockEmailService : EmailService<SimpleMailMessage> {
    override fun sendEmail(message: SimpleMailMessage) {
        if (logger.isLoggable(Level.INFO)) logger.info(message.toString())
    }

    companion object {
        private val logger = Logger.getLogger(MockEmailService::class.java.name)
    }
}
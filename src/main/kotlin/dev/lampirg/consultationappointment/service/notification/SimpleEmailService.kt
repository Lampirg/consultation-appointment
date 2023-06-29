package dev.lampirg.consultationappointment.service.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.stereotype.Service

@Service
@Profile("email & prod")
class SimpleEmailService @Autowired constructor(private val mailSender: MailSender) : EmailService<SimpleMailMessage> {
    override fun sendEmail(message: SimpleMailMessage) {
        mailSender.send(message)
    }
}
package dev.lampirg.consultationappointment.service.notification

import org.springframework.mail.MailMessage

interface EmailService<T : MailMessage> {
    fun sendEmail(message: T)
}
package dev.lampirg.consultationappointment.service.notification;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("email")
public class NotificationAspect {

    private EmailService emailService;

    public NotificationAspect(EmailService emailService) {
        this.emailService = emailService;
    }

    // TODO: advices for cancelling appointment and consultation

    @After("execution(* dev.lampirg.consultationappointment.service.student.AppointmentMaker.makeAppointment(..)) && " +
            "args(teacher,..,datePeriod)")
    public void notifyTeacherAboutAppointment(Teacher teacher, DatePeriod datePeriod) {
        if (!datePeriod.getAppointments().isEmpty())
            return;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("consultation_service@pgups.example");
        message.setSubject("ПГУПС: запись на консультацию");
        message.setTo(teacher.getEmail());
        String text = """
                Здравствуйте, %s %s %s.
                На вашу консультацию записался студент.
                """;
        message.setText(String.format("Здравствуйте, %s %s %s."));
        emailService.sendEmail(message);
    }

}

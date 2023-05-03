package dev.lampirg.consultationappointment.service.notification;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("email")
@Async
public class NotificationAspect {

    private EmailService<SimpleMailMessage> emailService;

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
                На вашу консультацию, которая состоится %td.%tm с %tR до %tR записался студент.
                """;
        message.setText(String.format(text,
                teacher.getLastName(), teacher.getFirstName(), teacher.getPatronymic(),
                datePeriod.getStartTime().toLocalDate(), datePeriod.getStartTime().toLocalDate(),
                datePeriod.getStartTime().toLocalTime(), datePeriod.getEndTime().toLocalTime()
        ));
        emailService.sendEmail(message);
    }

}

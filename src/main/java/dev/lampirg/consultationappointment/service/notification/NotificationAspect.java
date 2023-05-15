package dev.lampirg.consultationappointment.service.notification;

import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Profile("email")
@Async
public class NotificationAspect {

    private EmailService<SimpleMailMessage> emailService;
    private TeacherRepository teacherRepository;

    public NotificationAspect(EmailService<SimpleMailMessage> emailService, TeacherRepository teacherRepository) {
        this.emailService = emailService;
        this.teacherRepository = teacherRepository;
    }

    @After("execution(* dev.lampirg.consultationappointment.service.student.AppointmentMaker.makeAppointment(..)) && " +
            "args(teacher,..,datePeriod)")
    @Transactional(readOnly = true)
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

    @After("execution(* dev.lampirg.consultationappointment.service.teacher.ConsultationMaker.deleteConsultation(..))")
    @Transactional(readOnly = true)
    public void notifyStudentsAboutDeletion(JoinPoint joinPoint) throws Throwable {
        DatePeriod datePeriod = (DatePeriod) joinPoint.getArgs()[1];
        SimpleMailMessage template = new SimpleMailMessage();
        template.setFrom("consultation_service@pgups.example");
        template.setSubject("ПГУПС: запись на консультацию");
        String text = """
                Здравствуйте, %s %s %s.
                К сожалению, консультация на которую вы записались (преподаватель: %s %s %s, дата: %td.%tm, время: с %tR до %tR), была отменена.
                """;


        datePeriod.getAppointments().forEach((appointment -> {
            Student student = appointment.getStudent();
            Teacher teacher = appointment.getTeacher();
            template.setTo(student.getEmail());
            template.setText(String.format(text,
                    student.getLastName(), student.getFirstName(), student.getPatronymic(),
                    teacher.getLastName(), teacher.getFirstName(), teacher.getPatronymic(),
                    datePeriod.getStartTime().toLocalDate(), datePeriod.getStartTime().toLocalDate(),
                    datePeriod.getStartTime().toLocalTime(), datePeriod.getEndTime().toLocalTime()
            ));
            emailService.sendEmail(template);
        }));
    }

}

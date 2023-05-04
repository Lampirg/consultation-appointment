package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Aspect
@Component
public class AutoDeleteConsultationAspect {

    private DatePeriod datePeriod;
    private TeacherRepository teacherRepository;
    private ConsultationMaker consultationMaker;
    private TaskScheduler scheduler;

    public AutoDeleteConsultationAspect(TeacherRepository teacherRepository, ConsultationMaker consultationMaker, TaskScheduler scheduler) {
        this.teacherRepository = teacherRepository;
        this.consultationMaker = consultationMaker;
        this.scheduler = scheduler;
    }

    @AfterReturning("execution(* dev.lampirg.consultationappointment.service.teacher.ConsultationMaker.createConsultation(..)) && " +
            "args(teacher,datePeriod)")
    public void scheduleConsultationDeletion(Teacher teacher, DatePeriod datePeriod) {
        Instant instant = datePeriod.getEndTime().atZone(ZoneId.of("Europe/Moscow")).toInstant();
        scheduler.schedule(() -> {
            Optional<DatePeriod> freshDatePeriod = teacherRepository.findById(teacher.getId()).orElseThrow().getDatePeriods()
                    .stream()
                    .filter((teacherDatePeriod) -> teacherDatePeriod.getStartTime().isEqual(datePeriod.getStartTime()) &&
                            teacherDatePeriod.getEndTime().isEqual(datePeriod.getEndTime()))
                    .findAny();
            if (freshDatePeriod.isPresent() && freshDatePeriod.get().getEndTime().isEqual(datePeriod.getEndTime()))
                consultationMaker.deleteConsultationById(teacherRepository.findById(teacher.getId()).orElseThrow(), freshDatePeriod.get().getId());
        }, instant);
    }

}

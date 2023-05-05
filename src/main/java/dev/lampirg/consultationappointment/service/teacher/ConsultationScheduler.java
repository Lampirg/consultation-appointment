package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class ConsultationScheduler {

    ConsultationMaker consultationMaker;
    private TaskScheduler scheduler;

    public ConsultationScheduler(ConsultationMaker consultationMaker, TaskScheduler scheduler) {
        this.consultationMaker = consultationMaker;
        this.scheduler = scheduler;
    }

    public void addPattern(ConsultationPattern pattern) {
        Instant instant = LocalDateTime.of(pattern.getConsultationInfo().getDate(),
                pattern.getConsultationInfo().getEndTime()).atZone(ZoneId.of("Europe/Moscow")).toInstant();
        scheduler.scheduleWithFixedDelay(() -> {
            DatePeriod datePeriod = new DatePeriod(pattern.getConsultationInfo().getClassroom(),
                    LocalDateTime.of(LocalDate.now().plusDays(7), pattern.getConsultationInfo().getStartTime()),
                    LocalDateTime.of(LocalDate.now().plusDays(7), pattern.getConsultationInfo().getEndTime()));
            consultationMaker.createConsultation(pattern.getTeacher(), datePeriod);
        }, instant, Duration.ofDays(7));
    }

}

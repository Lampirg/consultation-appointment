package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
public class ConsultationScheduler {

    private ConsultationMaker consultationMaker;
    private TaskScheduler scheduler;
    private Map<Integer, List<ConsultationPattern>> patternsMap = new HashMap<>();
    private Map<ConsultationPattern, ScheduledFuture<?>> schedulesMap = new HashMap<>();

    public ConsultationScheduler(ConsultationMaker consultationMaker, TaskScheduler scheduler) {
        this.consultationMaker = consultationMaker;
        this.scheduler = scheduler;
    }

    public void addPattern(ConsultationPattern pattern) {
        patternsMap.putIfAbsent(pattern.getTeacher().getId(), new ArrayList<>());
        Instant instant = LocalDateTime.of(pattern.getConsultationInfo().getDate(),
                pattern.getConsultationInfo().getEndTime())
                .atZone(ZoneId.of("Europe/Moscow")).toInstant();
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            DatePeriod datePeriod = new DatePeriod(pattern.getConsultationInfo().getClassroom(),
                    LocalDateTime.of(LocalDate.now().plusDays(7), pattern.getConsultationInfo().getStartTime()),
                    LocalDateTime.of(LocalDate.now().plusDays(7), pattern.getConsultationInfo().getEndTime()));
            consultationMaker.createConsultation(pattern.getTeacher(), datePeriod);
        }, instant, Duration.ofDays(7));
        patternsMap.get(pattern.getTeacher().getId()).add(pattern);
        schedulesMap.put(pattern, future);
        scheduleRemove(pattern);
    }

    public Optional<List<ConsultationPattern>> getTeacherPatterns(Teacher teacher) {
        return Optional.ofNullable(patternsMap.get(teacher.getId()));
    }

    public void removePattern(ConsultationPattern pattern) {
        schedulesMap.get(pattern).cancel(false);
        schedulesMap.remove(pattern);
        patternsMap.get(pattern.getTeacher().getId()).remove(pattern);
    }

    private void scheduleRemove(ConsultationPattern pattern) {
        Instant instant = LocalDateTime.of(pattern.getUntil(), LocalTime.MAX)
                .atZone(ZoneId.of("Europe/Moscow")).toInstant();
        scheduler.schedule(() -> removePattern(pattern), instant);
    }

}

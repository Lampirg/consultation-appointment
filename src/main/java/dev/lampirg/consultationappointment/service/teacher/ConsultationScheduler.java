package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.schedule.PatternSchedule;
import dev.lampirg.consultationappointment.data.schedule.PatternScheduleRepository;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

@Service
public class ConsultationScheduler {

    private static class Pair {
        ScheduledFuture<?> start;
        ScheduledFuture<?> end;
        public Pair(ScheduledFuture<?> start) {
            this.start = start;
        }
        public List<ScheduledFuture<?>> getAll() {
            return List.of(start, end);
        }
    }

    private PatternScheduleRepository patternScheduleRepository;
    private ConsultationMaker consultationMaker;
    private TaskScheduler scheduler;
    private Map<Integer, List<ConsultationPattern>> patternsMap = new HashMap<>();
    private Map<ConsultationPattern, Pair> schedulesMap = new HashMap<>();
    private Map<ConsultationPattern, PatternSchedule> patternScheduleMap = new HashMap<>();

    public ConsultationScheduler(PatternScheduleRepository patternScheduleRepository, ConsultationMaker consultationMaker, TaskScheduler scheduler) {
        this.patternScheduleRepository = patternScheduleRepository;
        this.consultationMaker = consultationMaker;
        this.scheduler = scheduler;
    }

    @Transactional
    public void savePattern(ConsultationPattern pattern) {
        PatternSchedule schedule = new PatternSchedule(pattern);
        patternScheduleRepository.save(schedule);
        patternScheduleMap.put(pattern, schedule);
    }

    public void addPattern(ConsultationPattern pattern) {
        while (pattern.getConsultationInfo().getDate().isBefore(LocalDate.now()))
            pattern.getConsultationInfo().setDate(pattern.getConsultationInfo().getDate().plusDays(7));
        patternsMap.putIfAbsent(pattern.getTeacher().getId(), new ArrayList<>());
        Instant instant = LocalDateTime.of(pattern.getConsultationInfo().getDate(),
                pattern.getConsultationInfo().getEndTime())
                .atZone(ZoneId.of("Europe/Moscow")).toInstant();
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> {
            pattern.getConsultationInfo().setDate(pattern.getConsultationInfo().getDate().plusDays(7));
            DatePeriod datePeriod = new DatePeriod(pattern.getConsultationInfo().getClassroom(),
                    LocalDateTime.of(pattern.getConsultationInfo().getDate(), pattern.getConsultationInfo().getStartTime()),
                    LocalDateTime.of(pattern.getConsultationInfo().getDate(), pattern.getConsultationInfo().getEndTime()));
            consultationMaker.createConsultation(pattern.getTeacher(), datePeriod);
        }, instant, Duration.ofDays(7));
        patternsMap.get(pattern.getTeacher().getId()).add(pattern);
        schedulesMap.put(pattern, new Pair(future));
        scheduleRemove(pattern);
    }

    public Optional<List<ConsultationPattern>> getTeacherPatterns(Teacher teacher) {
        return Optional.ofNullable(patternsMap.get(teacher.getId()));
    }

    @Transactional
    public void removePattern(ConsultationPattern pattern) {
        schedulesMap.get(pattern).getAll().forEach(future -> future.cancel(false));
        schedulesMap.remove(pattern);
        patternsMap.get(pattern.getTeacher().getId()).remove(pattern);
        patternScheduleRepository.delete(patternScheduleMap.get(pattern));
        patternScheduleMap.remove(pattern);
    }

    private void scheduleRemove(ConsultationPattern pattern) {
        Instant instant = LocalDateTime.of(pattern.getUntil(), LocalTime.MAX)
                .atZone(ZoneId.of("Europe/Moscow")).toInstant();
        scheduler.schedule(() -> removePattern(pattern), instant);
    }

}

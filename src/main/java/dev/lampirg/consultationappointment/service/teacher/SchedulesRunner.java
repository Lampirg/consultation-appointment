package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.schedule.PatternSchedule;
import dev.lampirg.consultationappointment.data.schedule.PatternScheduleRepository;
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class SchedulesRunner {

    private PatternScheduleRepository patternScheduleRepository;
    private ConsultationScheduler consultationScheduler;

    public SchedulesRunner(PatternScheduleRepository patternScheduleRepository, ConsultationScheduler consultationScheduler) {
        this.patternScheduleRepository = patternScheduleRepository;
        this.consultationScheduler = consultationScheduler;
    }

    @Bean
    @Order(20)
    public CommandLineRunner runSchedules() {
        return args -> {
            List<PatternSchedule> schedules = patternScheduleRepository.findAll();
            schedules.forEach((schedule) -> consultationScheduler.addPattern(ConsultationPattern.fromPatternSchedule(schedule)));
        };
    }
}

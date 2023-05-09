package dev.lampirg.consultationappointment.service.teacher;

import dev.lampirg.consultationappointment.data.schedule.PatternSchedule;
import dev.lampirg.consultationappointment.data.schedule.PatternScheduleRepository;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.data.teacher.TeacherRepository;
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
    private TeacherRepository teacherRepository;
    private AutoDeleteConsultationAspect deleteConsultationAspect;

    public SchedulesRunner(PatternScheduleRepository patternScheduleRepository, ConsultationScheduler consultationScheduler, TeacherRepository teacherRepository, AutoDeleteConsultationAspect deleteConsultationAspect) {
        this.patternScheduleRepository = patternScheduleRepository;
        this.consultationScheduler = consultationScheduler;
        this.teacherRepository = teacherRepository;
        this.deleteConsultationAspect = deleteConsultationAspect;
    }

    @Bean
    @Order(20)
    public CommandLineRunner runSchedules() {
        return args -> {
            List<PatternSchedule> schedules = patternScheduleRepository.findAll();
            schedules.forEach((schedule) -> consultationScheduler.addPattern(ConsultationPattern.fromPatternSchedule(schedule)));
            List<Teacher> teachers = teacherRepository.findAll();
            teachers.forEach(teacher -> teacher.getDatePeriods().forEach(datePeriod -> deleteConsultationAspect.scheduleConsultationDeletion(teacher, datePeriod)));
        };
    }
}

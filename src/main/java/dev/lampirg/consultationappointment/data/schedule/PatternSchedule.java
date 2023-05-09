package dev.lampirg.consultationappointment.data.schedule;

import dev.lampirg.consultationappointment.data.teacher.Teacher;
import dev.lampirg.consultationappointment.web.fetch.ConsultationPattern;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class PatternSchedule {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate until;

    private LocalDate fromDate;

    private LocalDate date;

    private String classroom;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public static PatternSchedule fromConsultationPattern(ConsultationPattern pattern) {
        PatternSchedule schedule = new PatternSchedule();
        schedule.setFromDate(pattern.getFrom());
        schedule.setClassroom(pattern.getConsultationInfo().getClassroom());
        schedule.setDate(pattern.getConsultationInfo().getDate());
        schedule.setTeacher(pattern.getTeacher());
        schedule.setEndTime(pattern.getConsultationInfo().getEndTime());
        schedule.setStartTime(pattern.getConsultationInfo().getStartTime());
        schedule.setUntil(pattern.getUntil());
        return schedule;
    }
}
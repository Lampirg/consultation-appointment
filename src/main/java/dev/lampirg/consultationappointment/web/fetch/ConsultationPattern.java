package dev.lampirg.consultationappointment.web.fetch;

import dev.lampirg.consultationappointment.data.schedule.PatternSchedule;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ConsultationPattern {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate until;
    private LocalDate from;
    private ConsultationInfo consultationInfo;
    private Teacher teacher;

    public static ConsultationPattern fromPatternSchedule(PatternSchedule schedule) {
        ConsultationPattern pattern = new ConsultationPattern();
        pattern.setFrom(schedule.getFromDate());
        pattern.setUntil(schedule.getUntil());
        pattern.setTeacher(schedule.getTeacher());
        ConsultationInfo info = new ConsultationInfo();
        info.setDate(schedule.getDate());
        info.setClassroom(schedule.getClassroom());
        info.setStartTime(schedule.getStartTime());
        info.setEndTime(schedule.getEndTime());
        pattern.setConsultationInfo(info);
        return pattern;
    }

}

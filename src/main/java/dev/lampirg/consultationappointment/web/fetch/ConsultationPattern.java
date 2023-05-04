package dev.lampirg.consultationappointment.web.fetch;

import dev.lampirg.consultationappointment.data.appointment.pattern.AppointmentPattern;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ConsultationPattern {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate until;
    private ConsultationInfo consultationInfo;
    private Teacher teacher;

    public AppointmentPattern toAppointmentPattern() {
        AppointmentPattern pattern = new AppointmentPattern();
        pattern.setTeacher(teacher);
        pattern.setFrom(consultationInfo.getDate());
        pattern.setUntil(until);
        pattern.setDayOfWeek(consultationInfo.getDate().getDayOfWeek());
        pattern.setClassroom(consultationInfo.getClassroom());
        pattern.setStartTime(consultationInfo.getStartTime());
        pattern.setEndTime(consultationInfo.getEndTime());
        return pattern;
    }

}

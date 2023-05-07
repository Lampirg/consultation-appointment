package dev.lampirg.consultationappointment.web.fetch;

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

}

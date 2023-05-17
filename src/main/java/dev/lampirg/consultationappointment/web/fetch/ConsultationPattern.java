package dev.lampirg.consultationappointment.web.fetch;

import dev.lampirg.consultationappointment.data.teacher.Teacher;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Embeddable
public class ConsultationPattern {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate until;
    private LocalDate fromDate;
    @Embedded
    private ConsultationInfo consultationInfo;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}

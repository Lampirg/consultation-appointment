package dev.lampirg.consultationappointment.data.appointment;

import dev.lampirg.consultationappointment.data.appointment.validator.InTimeBorders;
import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@InTimeBorders
public class Appointment extends AbstractPersistable<Long> {

    @ManyToOne(cascade = CascadeType.MERGE)
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Student student;

    @ManyToOne
    private DatePeriod appointmentPeriod;

    private LocalDateTime startTime;
}
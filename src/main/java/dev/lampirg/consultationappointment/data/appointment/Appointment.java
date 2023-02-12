package dev.lampirg.consultationappointment.data.appointment;

import dev.lampirg.consultationappointment.data.student.Student;
import dev.lampirg.consultationappointment.data.teacher.DatePeriod;
import dev.lampirg.consultationappointment.data.teacher.Teacher;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "appointment")
public class Appointment extends AbstractPersistable<Long> {

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Student student;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private DatePeriod appointmentPeriod;

    private LocalDateTime startTime;
}
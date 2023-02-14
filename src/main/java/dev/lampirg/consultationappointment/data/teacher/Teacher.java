package dev.lampirg.consultationappointment.data.teacher;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.model.Person;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends Person {

    @OneToMany(cascade = CascadeType.ALL)
    private Set<DatePeriod> datePeriod = new HashSet<>();

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Appointment> appointment;
}
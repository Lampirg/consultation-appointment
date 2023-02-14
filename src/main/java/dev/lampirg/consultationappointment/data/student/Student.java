package dev.lampirg.consultationappointment.data.student;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.model.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Student extends Person {

    @NotEmpty
    @Pattern(regexp = "[А-Я]{3}-[0-9]{3}")
    private String groupName;

    @OneToMany(mappedBy = "student", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<Appointment> appointment;
}

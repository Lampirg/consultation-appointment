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

    public Student(String firstName, String lastName, String patronymic, String email, String groupName) {
        setFirstName(firstName);
        setLastName(lastName);
        setPatronymic(patronymic);
        setEmail(email);
        setGroupName(groupName);
    }

    @NotEmpty
    @Pattern(regexp = "[А-Я]{3}-[0-9]{3}")
    private String groupName;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Appointment> appointment;
}

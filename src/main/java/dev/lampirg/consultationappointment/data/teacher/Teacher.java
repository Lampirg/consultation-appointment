package dev.lampirg.consultationappointment.data.teacher;

import dev.lampirg.consultationappointment.data.model.Person;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
}
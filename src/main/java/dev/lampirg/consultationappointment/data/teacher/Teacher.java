package dev.lampirg.consultationappointment.data.teacher;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.model.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends Person {

    public Teacher(@NotEmpty String firstName, @NotEmpty String lastName, String patronymic, @Email String email, String password) {
        super(firstName, lastName, patronymic, email, password);
    }

    @OneToMany(cascade = CascadeType.ALL)
    private Set<DatePeriod> datePeriods = new HashSet<>();

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private Set<Appointment> appointment = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_TEACHER"));
    }
}
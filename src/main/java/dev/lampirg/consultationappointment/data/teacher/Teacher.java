package dev.lampirg.consultationappointment.data.teacher;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.model.Person;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DatePeriod> datePeriods = new HashSet<>();

    @OneToMany(mappedBy = "teacher")
    private Set<Appointment> appointments = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_TEACHER"));
    }
}
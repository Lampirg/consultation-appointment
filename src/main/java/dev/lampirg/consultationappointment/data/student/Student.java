package dev.lampirg.consultationappointment.data.student;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.model.Person;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student extends Person {

    public Student(@NotEmpty String firstName, @NotEmpty String lastName, String patronymic, @Email String email, String password, String groupName) {
        super(firstName, lastName, patronymic, email, password);
        this.groupName = groupName;
    }

    @NotEmpty
    @Pattern(regexp = "[А-Я]{3}-[0-9]{3}")
    private String groupName;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private Set<Appointment> appointments = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }
}

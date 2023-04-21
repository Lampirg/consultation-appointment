package dev.lampirg.consultationappointment.data.student;

import dev.lampirg.consultationappointment.data.appointment.Appointment;
import dev.lampirg.consultationappointment.data.model.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Student extends Person {

    public Student(@NotEmpty String firstName, @NotEmpty String lastName, String patronymic, @Email String email, String password, String groupName) {
        super(firstName, lastName, patronymic, email, password);
        this.groupName = groupName;
    }

    @NotEmpty
    @Pattern(regexp = "[А-Я]{3}-[0-9]{3}")
    private String groupName;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Appointment> appointment;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_STUDENT"));
    }
}

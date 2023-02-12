package dev.lampirg.consultationappointment.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Getter
@Setter
@MappedSuperclass
public class Person extends AbstractPersistable<Integer> {

    @NotEmpty

    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String patronymic;

    @Email
    private String email;

}

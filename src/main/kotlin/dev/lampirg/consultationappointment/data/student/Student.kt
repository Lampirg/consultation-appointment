package dev.lampirg.consultationappointment.data.student

import dev.lampirg.consultationappointment.data.appointment.Appointment
import dev.lampirg.consultationappointment.data.model.Person
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Entity
@Getter
@Setter
@NoArgsConstructor
open class Student(
    firstName: String,
    lastName: String,
    patronymic: String,
    email: String,
    password: String,
    @get:NotEmpty @get:Pattern(regexp = "[А-Я]{3}-[0-9]{3}")
    open var groupName: String,
    @OneToMany(mappedBy = "student", cascade = [CascadeType.REMOVE])
    open val appointments: MutableSet<Appointment> = HashSet()
) : Person(firstName, lastName, patronymic, email, password) {

    override fun getAuthorities(): Collection<GrantedAuthority?> = listOf(SimpleGrantedAuthority("ROLE_STUDENT"))
}
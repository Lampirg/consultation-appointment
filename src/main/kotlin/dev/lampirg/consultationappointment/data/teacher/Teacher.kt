package dev.lampirg.consultationappointment.data.teacher

import dev.lampirg.consultationappointment.data.appointment.Appointment
import dev.lampirg.consultationappointment.data.model.Person
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
open class Teacher(
    firstName: String,
    lastName: String,
    patronymic: String,
    email: String,
    password: String,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var datePeriods: MutableSet<DatePeriod> = mutableSetOf(),
    @OneToMany(mappedBy = "teacher")
    val appointments: MutableSet<Appointment> = mutableSetOf()
) :
    Person(firstName, lastName, patronymic, email, password) {

    override fun getAuthorities(): Collection<GrantedAuthority?> = listOf(SimpleGrantedAuthority("ROLE_TEACHER"))
}
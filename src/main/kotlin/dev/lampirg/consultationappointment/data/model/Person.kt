package dev.lampirg.consultationappointment.data.model

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import org.springframework.data.jpa.domain.AbstractPersistable
import org.springframework.security.core.userdetails.UserDetails

@Getter
@Setter
@AllArgsConstructor
@MappedSuperclass
@NoArgsConstructor
abstract class Person(
    @get:NotEmpty
    open var firstName: String,
    @get:NotEmpty
    open var lastName: String,
    open var patronymic: String,
    @get:Email
    @Column(unique = true)
    open var email: String,
    @get:JvmName("password") open var password: String,

) : AbstractPersistable<Int>(), UserDetails {


    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
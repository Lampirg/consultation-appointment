package dev.lampirg.consultationappointment.security

import dev.lampirg.consultationappointment.data.model.Person
import dev.lampirg.consultationappointment.data.model.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*

class PersonDetailService<T : Person> @Autowired constructor(private val personRepository: PersonRepository<T>) : UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails =
        Optional.ofNullable(personRepository.findByEmail(username))
            .orElseThrow { UsernameNotFoundException("User $username not found") }
}
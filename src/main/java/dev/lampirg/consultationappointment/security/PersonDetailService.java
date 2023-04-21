package dev.lampirg.consultationappointment.security;

import dev.lampirg.consultationappointment.data.model.Person;
import dev.lampirg.consultationappointment.data.model.PersonRepository;
import dev.lampirg.consultationappointment.data.student.StudentRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class PersonDetailService<T extends Person> implements UserDetailsService {

    private PersonRepository<T> personRepository;

    public PersonDetailService(PersonRepository<T> personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(personRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}

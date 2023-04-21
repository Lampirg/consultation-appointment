package dev.lampirg.consultationappointment.data.student;

import dev.lampirg.consultationappointment.data.model.PersonRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends PersonRepository<Student> {
}